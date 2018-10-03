/********************************************************************************
 * Copyright (c) 2015-2016 GE Digital. All rights reserved.                     *
 *                                                                              *
 * The copyright to the computer software herein is the property of GE Digital. *
 * The software may be used and/or copied only with the written permission of   *
 * GE Digital or in accordance with the terms and conditions stipulated in the  *
 * agreement/contract under which the software has been supplied.               *
 ********************************************************************************/

package iiotbook.chapter8.apigateway.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rx.Observable;

import iiotbook.chapter8.apigateway.model.Analytic;
import iiotbook.chapter8.apigateway.model.AnalyticResources;
import iiotbook.chapter8.apigateway.utils.Utils;

@Service
public class AnalyticService extends ServiceBase {
    private static final Logger logger = LoggerFactory.getLogger(AnalyticService.class);

    @Value("${demo.smartshops.url}")
    private String smartShopsUrl;

    /**
     * Gets all analytics
     * @return Observable<List<Analytic>>
     */
    public Observable<List<Analytic>> getAnalytics() {
        logger.info("*** inside getAnalytics()  ***");

        List<Analytic> analyticList = new ArrayList<>();

        try {
            String assetUrl = smartShopsUrl + "/analytics";

            RestTemplate restTemplate = getRestTemplate();
            HttpEntity entity = getHttpEntity();

            AnalyticResources analytics =
                restTemplate.exchange(assetUrl, HttpMethod.GET, entity, AnalyticResources.class).getBody();
            logger.info("\nREST_response:\n" + Utils.object2JsonWithPrettyFormat(analytics));

            Map<String, List<Analytic>> response =
                analytics.getContent().stream().collect(Collectors.groupingBy(Analytic::getName));

            Set<String> keys = response.keySet();
            for (String key : keys) {
                analyticList.addAll(response.get(key));
            }
        } catch (Exception e) {
            logger.error("Error occurs while calling getAnalytics().", e);
        }

        return makeAnObservable(analyticList);
    }
}
