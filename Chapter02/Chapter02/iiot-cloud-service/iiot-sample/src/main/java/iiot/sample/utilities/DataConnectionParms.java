package iiot.sample.utilities;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by 212568770 on 5/13/17.
 */

@Component
public class DataConnectionParms {

    @Getter
    @Value("${db_validation_interval_ms:30000}")
    private Integer validationInterval;

    @Getter
    @Value("${db_time_between_evictions_ms:5000}")
    private Integer timeBetweenEvictionRunMillis;

    @Getter
    @Value("${db_max_active:5}")
    private Integer maxActive;

    @Getter
    @Value("${db_inital:3}")
    private Integer initialSize;

    @Getter
    @Value("${db_max_wait:30000}")
    private Integer maxWait;

    @Getter
    @Value("${db_remove_abandoned_timeout_sec:240}")
    private Integer removeAbandonedTimeout;

    @Getter
    @Value("${db_evictable_idle_time_ms:30000}")
    private Integer minEvictableIdleTimeMillis;

    @Getter
    @Value("${db_max_idle:2}")
    private Integer maxIdle;

    @Getter
    @Value("${db_min_idle:1}")
    private Integer minIdle;


}
