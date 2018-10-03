package iiot.sample.config;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
public class GuavaCacheConfiguration {

    public static final String ACCESSIBLE_RESOURCES_CACHE = "accessibleResourcesCache";

    @Bean
    public GuavaCache accessibleResourcesCache() {

        return  new GuavaCache(ACCESSIBLE_RESOURCES_CACHE, CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .maximumSize(10000)
                .build());
    }

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(Arrays.asList(
                 accessibleResourcesCache()));
        return simpleCacheManager;
    }

}
