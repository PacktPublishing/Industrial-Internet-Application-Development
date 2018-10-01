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

import iiotbook.chapter8.apigateway.model.Asset;
import iiotbook.chapter8.apigateway.model.AssetResources;
import iiotbook.chapter8.apigateway.utils.Utils;

@Service
public class AssetService extends ServiceBase {
    private static final Logger logger = LoggerFactory.getLogger(AssetService.class);

    @Value("${demo.smartshops.url}")
    private String smartShopsUrl;

    /**
     * Gets all assets
     * @return Observable<List<Asset>>
     */
    public Observable<List<Asset>> getAssets() {
        logger.info("getAssets()");

        List<Asset> assetList = new ArrayList<>();

        try {
            String assetUrl = smartShopsUrl + "/assets";

            RestTemplate restTemplate = getRestTemplate();
            HttpEntity entity = getHttpEntity();

            AssetResources assets =
                restTemplate.exchange(assetUrl, HttpMethod.GET, entity, AssetResources.class).getBody();
            logger.info("\nREST_response:\n" + Utils.object2JsonWithPrettyFormat(assets));

            Map<String, List<Asset>> response =
                assets.getContent().stream().collect(Collectors.groupingBy(Asset::getOperator));
            Set<String> keys = response.keySet();
            for (String key : keys) {
                assetList.addAll(response.get(key));
            }
        } catch (Exception e) {
            logger.error("Error occurs while calling getAssets().", e);
        }

        return makeAnObservable(assetList);
    }
}
