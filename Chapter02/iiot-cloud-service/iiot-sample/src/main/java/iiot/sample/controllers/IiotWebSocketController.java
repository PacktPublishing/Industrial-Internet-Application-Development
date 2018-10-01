package iiot.sample.controllers;

/**
 * Created by 212367835 on 9/19/17.
 */

import iiot.sample.domain.persistence.entity.Alert;
import iiot.sample.service.IiotService;
import iiot.sample.service.model.Credentials;
import iiot.sample.utilities.StopWatchUtil;
import iiot.sample.utilities.TenantUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

@RestController
public class IiotWebSocketController {
    private static final Logger logger = LoggerFactory.getLogger(IiotWebSocketController.class);
    private final IiotService iiotService;

    @Autowired
    private TenantUtil tenantUtil;


    @Autowired
    public IiotWebSocketController(IiotService iiotService) {
        this.iiotService = iiotService;
    }



    @MessageMapping("/createAlert")
    @SendTo("/topic/alertCreated")
    public Alert createAlert(Alert alertMessage) throws Exception {
        StopWatch stopWatch = new StopWatch("create WebSocket using Alerts");
        logger.info("{Message Received}", new StopWatchUtil(stopWatch));

        //pass empty credentials since security and tenancy are not enabled
        Credentials credentials = new Credentials(null, tenantUtil.getTenantUuid());

        Alert createdAlert = iiotService.createAlerts(alertMessage,credentials,stopWatch);


        return createdAlert;
    }

}
