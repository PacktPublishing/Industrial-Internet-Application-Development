package iiot.sample.service;

import com.fasterxml.jackson.databind.JsonNode;
import iiot.sample.domain.persistence.entity.Alert;
import iiot.sample.repository.*;
import iiot.sample.service.model.*;
import iiot.sample.domain.error.ErrorCode;
import iiot.sample.domain.error.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;


import javax.transaction.Transactional;
import java.util.*;


@Service
@Slf4j
public class IiotService {
    private static final String FIND_ENTITY_TYPE = "listAlertEntity";

    private final AlertsRepository alertsRepository;

    @Autowired
    public IiotService(AlertsRepository alertsRepository ) {

        this.alertsRepository=alertsRepository;

    }

    @Transactional
    public Page<Alert> getAlerts(String tenantId, StopWatch stopWatch) {


        Pageable pageable = new PageRequest(0,10);
        stopWatch.start(FIND_ENTITY_TYPE);
        Page<Alert> alertsEntity = alertsRepository.findByTenantUuid(tenantId,pageable);
        stopWatch.stop();
        if (alertsEntity == null) {
            throw new ServiceException(ErrorCode.ENTITY_TYPE_DOES_NOT_EXIST, FIND_ENTITY_TYPE);
        }

        return alertsEntity;


    }

    @Transactional
    public Alert createAlerts(Alert alert, Credentials credentials, StopWatch stopWatch) {

        return alertsRepository.save(alert);
    }
}
