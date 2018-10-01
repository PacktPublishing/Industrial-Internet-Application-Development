package iiot.sample.utilities;

import com.fasterxml.jackson.databind.JsonNode;
import iiot.sample.domain.persistence.entity.JsonUtil;
import iiot.sample.service.model.Credentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.exceptions.JedisException;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by 212568770 on 4/22/17.
 */
@Slf4j
public class CacheManager {
    private static final String VALUE = "value";
    private static final String TS = "ts";
    private static final String REDIS_DOWN = "Redis Down";

    private final String repName;
    private final RedisTemplate redisTemplate;
    private final GuavaCache guavaCache;
    private final Long refreshMillis;

    private static final String appPrefix = "iiot-sample";
    private final ExecutorService tmpUserPermExecutor = new ThreadPoolExecutor(
            5,
            20,
            1,
            TimeUnit.MINUTES,
            new LinkedBlockingQueue<Runnable>(100),
            new CacheManager.CachePoolExecutor());

    public CacheManager(String repName, RedisTemplate redisTemplate, GuavaCache guavaCache, Long refreshMillis) {
        this.repName = repName;
        this.redisTemplate = redisTemplate;
        this.guavaCache = guavaCache;
        this.refreshMillis = refreshMillis;
    }


    protected Map<String, JsonNode> getMutipleFromCache(Credentials credentials, Collection<String> objectIds, RepGet repGet){
        Map<String, JsonNode> objectIdNodeMap = new HashMap<>();
        List<Object> allObjects = getAnyMutipleFromCache(credentials, objectIds);
        int position = 0;
        for (String objectId : objectIds){
            Map<String, String> redisData = (Map) allObjects.get(position);
            if (redisData == null || redisData.get(VALUE) == null || isTooOld(redisData.get(TS))){
                log.info("Too Old Condition {}", objectId);
                objectIdNodeMap.put(objectId, getFromCache(credentials, objectId, repGet));
            } else {
                log.info("New  Condition {}", objectId);
                objectIdNodeMap.put(objectId, JsonUtil.toJsonNodeQuietly(redisData.get(VALUE)));
            }
            position++;
        }
        return objectIdNodeMap;
    }

    private boolean isTooOld(String ts){
        Long oldTime = toLong(ts);
        if (oldTime == null){
            return true;
        }
        return   (System.currentTimeMillis()  - oldTime) >= this.refreshMillis;
    }

    private Long toLong(String ts){
        if (ts != null) {
            try {
                return Long.parseLong(ts);
            } catch (NumberFormatException e) {
                log.warn(String.format("Invalid number %s ", ts), e);
            }
        }
        return null;
    }

    private List<Object> getAnyMutipleFromCache(Credentials credentials, Collection<String> objectIds){
        try {
            return  redisTemplate.opsForHash().multiGet(redisCacheKey(credentials), createKeys(credentials, objectIds));
        }
        catch (JedisException | RedisConnectionFailureException |InvalidDataAccessApiUsageException e){
            log.error(REDIS_DOWN, e);
        }
        return Collections.emptyList();
    }


    protected JsonNode getFromCache(Credentials credentials, final String objectId,  final CacheManager.RepGet repGet) {
        final String key = createKey(credentials, objectId);

        JsonNode jsonNode;
        Map<String, String> redisData = getAnyNodeFromCache(credentials, key);
        if (redisData == null) {
            jsonNode = repGet.get(objectId, false);
            if (jsonNode != null) {
                putAnyNodeIntoCache(credentials, key, jsonNode);
            }
        } else {
            jsonNode = JsonUtil.toJsonNodeQuietly(redisData.get(VALUE));
            tmpUserPermExecutor.execute(() -> {
                JsonNode refreshNode = repGet.get(objectId, true);
                putAnyNodeIntoCache(credentials, key, refreshNode);
            });
        }
        return jsonNode;
    }


    private Collection<String> createKeys(Credentials credentials, Collection<String> objectIds){
        return objectIds.stream().map(p ->  createKey(credentials, p)).collect(Collectors.toList());
    }

    private String createKey(Credentials credentials, String objectId) {
        return String.format("%s_%s_%s_%s", credentials.getTenantUuid(), credentials.getUsername(), repName, objectId);
    }


    private void putAnyNodeIntoCache(final Credentials credentials, final String key, JsonNode jsonNode) {
        String anyJsonStr = JsonUtil.asStringQuitely(jsonNode);
        if (anyJsonStr != null) {
            Map<String, String> redisData = new HashMap<>();
            redisData.put(TS, String.valueOf(System.currentTimeMillis()));
            redisData.put(VALUE, anyJsonStr);
            try {
                redisTemplate.opsForHash().put(redisCacheKey(credentials), key, redisData);
            } catch (JedisException | RedisConnectionFailureException | InvalidDataAccessApiUsageException e) {
                log.error(REDIS_DOWN, e);
            }
            guavaCache.put(key, redisData);
        }
    }

    private Map<String, String> getAnyNodeFromCache(final Credentials credentials, String key) {
        Map<String, String> redisData = null;
        try {
           redisData = (Map) redisTemplate.opsForHash().get(redisCacheKey(credentials), key);
        } catch (JedisException | RedisConnectionFailureException | InvalidDataAccessApiUsageException e) {
            log.error("Redis down ", e);
        }
        if (redisData == null) {
            redisData = guavaCache.get(key, Map.class);
        }
        return redisData;

    }

    private String redisCacheKey(Credentials credentials) {
        return String.format("%s-%s", appPrefix, credentials.getTenantUuid());
    }

    @FunctionalInterface
    public static interface CachePut {
        public void put(String s);
    }

    @FunctionalInterface
    public static interface RepGet {
        public JsonNode get(String objectId, boolean refreshPhase);
    }

    private static class CachePoolExecutor implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            log.warn("CachePoolExecutor scheduling rejected ");
        }
    }

}
