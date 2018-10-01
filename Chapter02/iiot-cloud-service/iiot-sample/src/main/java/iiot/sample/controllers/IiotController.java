package iiot.sample.controllers;


import com.fasterxml.jackson.databind.JsonNode;
import iiot.sample.domain.persistence.entity.Alert;
import iiot.sample.service.IiotService;
import iiot.sample.service.model.*;
import iiot.sample.utilities.StopWatchUtil;
import iiot.sample.utilities.TenantUtil;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/iiotsample")
@Slf4j
public class IiotController {
    private static final Logger logger = LoggerFactory.getLogger(IiotController.class);
    private final IiotService iiotService;

    @Autowired
    private TenantUtil tenantUtil;

    @Autowired
    public IiotController(IiotService iiotService) {
        this.iiotService = iiotService;
    }


    @RequestMapping(value = "/alerts", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public  ResponseEntity<Page<Alert>> getListofAlerts(
            @RequestHeader(value="Authorization",required=false) String authorization,
            @RequestHeader(value="referer", required = false) String referer, HttpServletRequest request) {

        logger.error("referer {}", referer);
        StopWatch stopWatch = new StopWatch("getList of Alerts");
        Credentials credentials = new Credentials(authorization, tenantUtil.getTenantUuid());
        credentials.setReferer(referer);
        Page<Alert> listofAlerts = iiotService.getAlerts(tenantUtil.getTenantUuid(),stopWatch);
        logger.info("{}", new StopWatchUtil(stopWatch));
        return new ResponseEntity<>(listofAlerts, HttpStatus.OK);
    }



    @RequestMapping(value = "/alert", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public  ResponseEntity<Alert> getAlert(
            @RequestBody Alert alert,
            @RequestHeader(value="Authorization",required=false) String authorization,
            @RequestHeader(value="referer", required = false) String referer, HttpServletRequest request) {

        logger.error("referer {}", referer);
        StopWatch stopWatch = new StopWatch("Create Alert");
        Credentials credentials = new Credentials(authorization, tenantUtil.getTenantUuid());
        credentials.setReferer(referer);
        Alert createdAlert = iiotService.createAlerts(alert,credentials,stopWatch);
        logger.info("{}", new StopWatchUtil(stopWatch));
        return new ResponseEntity<>(createdAlert, HttpStatus.CREATED);
    }
}
