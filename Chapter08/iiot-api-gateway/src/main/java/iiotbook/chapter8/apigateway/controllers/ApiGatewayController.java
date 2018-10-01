/********************************************************************************
 * Copyright (c) 2015-2016 GE Digital. All rights reserved.                     *
 *                                                                              *
 * The copyright to the computer software herein is the property of GE Digital. *
 * The software may be used and/or copied only with the written permission of   *
 * GE Digital or in accordance with the terms and conditions stipulated in the  *
 * agreement/contract under which the software has been supplied.               *
 ********************************************************************************/

package iiotbook.chapter8.apigateway.controllers;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import rx.Observable;
import rx.Observer;

import iiotbook.chapter8.apigateway.model.Analytic;
import iiotbook.chapter8.apigateway.model.Asset;
import iiotbook.chapter8.apigateway.model.Shop;
import iiotbook.chapter8.apigateway.model.ShopResponse;
import iiotbook.chapter8.apigateway.services.AnalyticService;
import iiotbook.chapter8.apigateway.services.AssetService;
import iiotbook.chapter8.apigateway.services.ShopService;
import iiotbook.chapter8.apigateway.utils.Utils;

@RestController
@RequestMapping(value = "/gateway")
public class ApiGatewayController {
    private static final Logger logger = LoggerFactory.getLogger(ApiGatewayController.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static Boolean isCloudDeployed = null;

    @Autowired
    HttpServletRequest request;

    @Autowired
    private ShopService shopService;
    @Autowired
    private AssetService assetService;
    @Autowired
    private AnalyticService analyticService;

    public static boolean isIsCloudDeployed() {
        return isCloudDeployed == null ? false : isCloudDeployed;
    }

    @RequestMapping(value = "/v1/shop", method = RequestMethod.GET)
    public DeferredResult<ShopResponse> getShopDetail(
        @RequestParam(value = "name", required = true) String shopName) {
        logger.info("*** inside getTenantDetail()  ***");

        long startTime = System.currentTimeMillis();

        if (isCloudDeployed == null) {
            String requestUrl = request.getRequestURL().toString();
            isCloudDeployed = !requestUrl.contains("http://localhost:8080");
            logger.info("*** requestUrl:" + requestUrl);
        }

        return toDeferredResult(getShopDetailedResponse(shopName), startTime);
    }

    @SuppressWarnings("unchecked")
    private Observable<ShopResponse> getShopDetailedResponse(String shopName) {
        logger.info("*** inside getShopDetailedResponse()  ***");

        return Observable.zip(
            shopService.getShopByName(shopName),
            assetService.getAssets(),
            analyticService.getAnalytics(),
            (shop, assets, analytics) -> {
                ShopResponse shopResponse = new ShopResponse();
                shopResponse.setName(shop.getName());
                if (shop.getName() != null) {
                    shopResponse.setAssets((List<Asset>) assets);
                    shopResponse.setAnalytics((List<Analytic>) analytics);
                }
                return shopResponse;
            }
        );
    }

    public DeferredResult<ShopResponse> toDeferredResult(Observable<ShopResponse> detail, long startTime) {

        DeferredResult<ShopResponse> result = new DeferredResult<>();

        detail.subscribe(new Observer<ShopResponse>() {
            @Override
            public void onCompleted() {
                logger.info("*** inside OnCompleted ***");
                long endTime = System.currentTimeMillis();
                logger.info("==== GET tenant-Gateway::shopDetail() took [" + (endTime - startTime) + " ms]");
            }

            @Override
            public void onError(Throwable throwable) {
                logger.info("**inside OnError **");
                throwable.printStackTrace();

                ShopResponse emptyShopResponse = new ShopResponse();
                result.setResult(emptyShopResponse);
            }

            @Override
            public void onNext(ShopResponse shopResponse) {
                result.setResult(shopResponse);
                logger.info("***inside OnNext***");

                try {
                    logger.info("==========================================");
                    logger.info("\nFinal shop Response:\n" + Utils.object2JsonWithPrettyFormat(shopResponse));
                    logger.info("===========================================");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return result;
    }

    @RequestMapping(value = "/v1/shops", method = RequestMethod.GET)
    public DeferredResult<List<ShopResponse>> getAllShopDetails() {
        logger.info("*** inside getAllShopDetails()  ***");

        long startTime = System.currentTimeMillis();

        if (isCloudDeployed == null) {
            String requestUrl = request.getRequestURL().toString();
            isCloudDeployed = !requestUrl.contains("http://localhost:8080");
            logger.info("*** requestUrl:" + requestUrl);
        }

        return toDeferredResults(getAllShopDetailedResponse(), startTime);
    }

    @SuppressWarnings("unchecked")
    private Observable<List<ShopResponse>> getAllShopDetailedResponse() {
        logger.info("*** inside getAllShopDetailedResponse()  ***");

        return Observable.zip(
            shopService.getShops(),
            assetService.getAssets(),
            analyticService.getAnalytics(),
            (shops, assets, analytics) -> {
                List<ShopResponse> returnShopResponseList = new ArrayList<ShopResponse>();

                for (Shop shop : shops) {
                    ShopResponse shopResponse = new ShopResponse();
                    shopResponse.setName(shop.getName());
                    if (shop.getName() != null) {
                        shopResponse.setAssets((List<Asset>) assets);
                        shopResponse.setAnalytics((List<Analytic>) analytics);
                    }
                    returnShopResponseList.add(shopResponse);
                }
                return returnShopResponseList;
            }
        );
    }

    public DeferredResult<List<ShopResponse>> toDeferredResults(Observable<List<ShopResponse>> detail, long startTime) {

        DeferredResult<List<ShopResponse>> result = new DeferredResult<>();

        detail.subscribe(new Observer<List<ShopResponse>>() {
            @Override
            public void onCompleted() {
                logger.info("*** inside OnCompleted ***");
                long endTime = System.currentTimeMillis();
                logger.info("==== GET tenant-Gateway::allShopDetails() took [" + (endTime - startTime) + " ms]");
            }

            @Override
            public void onError(Throwable throwable) {
                logger.info("**inside OnError **");
                throwable.printStackTrace();

                List<ShopResponse> emptyShopResponseList = new ArrayList<ShopResponse>();
                result.setResult(emptyShopResponseList);
            }

            @Override
            public void onNext(List<ShopResponse> shopResponseList) {
                result.setResult(shopResponseList);
                logger.info("***inside OnNext***");

                try {
                    logger.info("==========================================");
                    logger.info("\nFinal shops Response:\n" + Utils.object2JsonWithPrettyFormat(shopResponseList));
                    logger.info("===========================================");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return result;
    }
}
