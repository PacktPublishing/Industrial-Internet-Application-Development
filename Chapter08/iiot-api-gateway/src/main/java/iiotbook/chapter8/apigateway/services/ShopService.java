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

import iiotbook.chapter8.apigateway.model.Shop;
import iiotbook.chapter8.apigateway.model.ShopResources;
import iiotbook.chapter8.apigateway.utils.Utils;

@Service
public class ShopService extends ServiceBase {
    private static final Logger logger = LoggerFactory.getLogger(ShopService.class);

    @Value("${demo.smartshops.url}")
    private String smartShopsUrl;

    /**
     * Gets shop by its name
     * @param name is the name of shop
     * @return Observable<Shop>
     */
    public Observable<Shop> getShopByName(String name) {
        logger.info("getShopByName(" + name + ")");

        Shop foundShop = null;

        List<Shop> shopList = getAllShops();
        for (Shop shop : shopList) {
            if (shop.getName().equals(name)) {
                foundShop = shop;
                break;
            }
        }

        foundShop = foundShop != null ? foundShop : new Shop();

        return makeAnObservable(foundShop);
    }

    /**
     * Gets all shops
     * @return Observable<List<Shop>>
     */
    public Observable<List<Shop>> getShops() {
        logger.info("getShops()");

        List<Shop> shopList = getAllShops();
        return makeAnObservable(shopList);
    }

    private List<Shop> getAllShops() {
        List<Shop> shopList = new ArrayList<>();

        try {
            String assetUrl = smartShopsUrl + "/shops";

            RestTemplate restTemplate = getRestTemplate();
            HttpEntity entity = getHttpEntity();

            ShopResources shops =
                restTemplate.exchange(assetUrl, HttpMethod.GET, entity, ShopResources.class).getBody();
            logger.info("\nREST_response:\n" + Utils.object2JsonWithPrettyFormat(shops));

            Map<String, List<Shop>> response =
                shops.getContent().stream().collect(Collectors.groupingBy(Shop::getName));

            Set<String> keys = response.keySet();
            for (String key : keys) {
                shopList.addAll(response.get(key));
            }
        } catch (Exception e) {
            logger.error("Error occurs while calling getAllShops().", e);
        }

        return shopList;
    }
}
