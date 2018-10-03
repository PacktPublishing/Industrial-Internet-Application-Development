package iiot.sample.repository;

import iiot.sample.domain.persistence.entity.Alert;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;


/**
 * Created by 212552609 on 3/16/17.
 */
public interface AlertsRepository extends CrudRepository<Alert, Long> {
    Alert findByAlertsUuidAndTenantUuid(String alertsUuid, String tenantUuid);
    Page<Alert> findByTenantUuid(String tenantUuid,Pageable pageable);
}
