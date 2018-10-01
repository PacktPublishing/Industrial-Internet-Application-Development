/********************************************************************************
 * Copyright (c) 2015-2016 GE Digital. All rights reserved.                     *
 *                                                                              *
 * The copyright to the computer software herein is the property of GE Digital. *
 * The software may be used and/or copied only with the written permission of   *
 * GE Digital or in accordance with the terms and conditions stipulated in the  *
 * agreement/contract under which the software has been supplied.               *
 ********************************************************************************/

package iiotbook.chapter8.apigateway.services;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Collections;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import iiotbook.chapter8.apigateway.controllers.ApiGatewayController;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class ServiceBase {

    protected RestTemplate getRestTemplate() {
        /**
         * smart-shops micoservices return response with media type "application/hal+json"
         */
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new Jackson2HalModule());

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/hal+json"));
        converter.setObjectMapper(mapper);

        RestTemplate restTemplate = new RestTemplate(Collections.<HttpMessageConverter<?>>singletonList(converter));

        /**
         * if you run the application in your local machine, then we need to set the proxy settings.
         */
        if (!ApiGatewayController.isIsCloudDeployed()) {
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            InetSocketAddress address = new InetSocketAddress("sjc1intproxy01.crd.ge.com", 8080);
            Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
            factory.setProxy(proxy);
            restTemplate.setRequestFactory(factory);
        }

        return restTemplate;
    }

    protected HttpEntity getHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<MultiValueMap> entity = new HttpEntity<>(headers);
        return entity;
    }

    protected <T> Observable<T> makeAnObservable(T... ts) {
        return Observable.create((Subscriber<? super T> s) -> {
            for (T t : ts) {
                s.onNext(t);
            }
            s.onCompleted();
        }).subscribeOn(Schedulers.io());
        // note the use of subscribeOn to make an otherwise synchronous Observable async
    }
}
