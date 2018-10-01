package iiot.chapter8.ciruitbreaker;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import iiot.chapter8.ciruitbreaker.dto.AssetDto;
import iiot.chapter8.ciruitbreaker.dto.Resources;
import com.google.common.collect.Maps;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GatewayManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayManager.class);

    @Autowired
    private RestTemplate restTemplate;
    @Value("${services.asset.url}")
    private String assetUrl;

    /**
     * Groups assets by customer
     *
     * @return
     */
    @HystrixCommand(fallbackMethod = "fallback")
    public ResponseEntity<Map<String, List<AssetDto>>> getAssetsByCustomer() {
        ResponseEntity<Resources> responseEntity = restTemplate.getForEntity(assetUrl + "/assets", Resources.class);
        Resources assets = responseEntity.getBody();
        Map<String, List<AssetDto>> response = assets.getContent().stream().collect(Collectors.groupingBy(AssetDto::getOperator));
        return okOrNotFound(response);
    }

    public ResponseEntity<Map<String, List<AssetDto>>> fallback(Throwable t) {
        List<AssetDto> assetDtoList = Arrays.asList(new AssetDto(1L, "Cached asset", "Test asset", "System", "System"));
        Map<String, List<AssetDto>> staticCache = Maps.newHashMap();
        staticCache.put("System", assetDtoList);
        LOGGER.warn("===================> Circit breaker:  Returning from callback method. Reason {}", t.getMessage(), t);
        return okOrNotFound(staticCache);
    }

    @Bean
    public RestTemplate restTemplate() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new Jackson2HalModule());

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/hal+json"));
        converter.setObjectMapper(mapper);

        return new RestTemplate(Collections.<HttpMessageConverter<?>>singletonList(converter));
    }

    /**
     * Return proper status code based on the response
     *
     * @param resourceMap
     * @param <K>
     * @param <V>
     * @return
     */
    private <K, V> ResponseEntity<Map<K, V>> okOrNotFound(Map<K, V> resourceMap) {
        return resourceMap != null && !resourceMap.isEmpty() ? ResponseEntity.ok(resourceMap) :
                new ResponseEntity<Map<K, V>>(HttpStatus.NOT_FOUND);
    }
}