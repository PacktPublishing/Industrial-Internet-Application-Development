/**
 * Copyright (c) 2016 General Electric Company. All rights reserved.
 * <p>
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package iiot.chapter8.ciruitbreaker;

import iiot.chapter8.ciruitbreaker.dto.AssetDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Gateway for aggregating microservices for a client
 */
@RestController
@RequestMapping(value = "/api")
public class GatewayController {

    @Autowired
    private GatewayManager manager;

    @RequestMapping(value = "/v1/customer-assets", method = GET)
    public ResponseEntity<Map<String, List<AssetDto>>> getAssetsByCustomer() {
        return manager.getAssetsByCustomer();
    }


}
