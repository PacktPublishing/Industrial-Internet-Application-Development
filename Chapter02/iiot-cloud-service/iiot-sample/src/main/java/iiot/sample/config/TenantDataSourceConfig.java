package iiot.sample.config;

import iiot.sample.utilities.DataConnectionParms;
import iiot.sample.utilities.DataSourceConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 212568770 on 4/16/17.
 */
@Component
@Slf4j
public class TenantDataSourceConfig {


    private final String dataSourceDriverClassName;


    private final String dataSourceUrl;


    private final String dataSourceUsername;


    private final String dataSourcePassword;


    private final DataSourceConfig[] dataSourceConfigs;
    private final Map<String, DataSourceConfig> configByTenantUuid = new HashMap<>();

    private final DataConnectionParms dataConnectionParms;

    @Autowired
    public TenantDataSourceConfig(@Value("${spring.datasource.driver-class-name:org.postgresql.Driver}") String dataSourceDriverClassName,
                                  @Value("${vcap.services.${iiot_sample_postgres_name:iiot-sample-postgres}.credentials.database}") String dbName,
                                  @Value("${vcap.services.${iiot_sample_postgres_name:iiot-sample-postgres}.credentials.hostname:null}") String hostname,
                                  @Value("${vcap.services.${iiot_sample_postgres_name:iiot-sample-postgres}.credentials.host:null}") String host,
                                  @Value("${vcap.services.${iiot_sample_postgres_name:iiot-sample-postgres}.credentials.port}") String port,
                                  @Value("${vcap.services.${iiot_sample_postgres_name:iiot-sample-postgres}.credentials.username}") String dataSourceUsername,
                                  @Value("${vcap.services.${iiot_sample_postgres_name:iiot-sample-postgres}.credentials.password}") String dataSourcePassword,
                                  DataConnectionParms dataConnectionParms) {
        this.dataSourceDriverClassName = dataSourceDriverClassName;
        String dbHost = (hostname == null || "null".equals(hostname)) ? host : hostname;
        this.dataSourceUrl = String.format("jdbc:postgresql://%s:%s/%s?ApplicationName=%s", dbHost, port, dbName, "iiotSample");
        log.info(String.format("connecting to jdbc %s", this.dataSourceUrl));
        this.dataSourceUsername = dataSourceUsername;
        this.dataSourcePassword = dataSourcePassword;
        this.dataConnectionParms = dataConnectionParms;
        this.dataSourceConfigs = loadDataSourceConfig();
        assert this.dataSourceConfigs != null;
        for (DataSourceConfig dataSourceConfig : dataSourceConfigs) {
            for (String tenantUuid : dataSourceConfig.getTenants()) {
                configByTenantUuid.put(tenantUuid, dataSourceConfig);
            }
        }
        log.warn("TenantDataSourceConfigs " + this.configByTenantUuid);
    }

    public DataSourceConfig getDataSourceConfig(String tenantUuid) {
        // we do not switch between data sources as of now
        log.debug("tenantUuid {}", tenantUuid);
        return configByTenantUuid.get("default");
    }

    public DataSourceConfig[] getDataSourceConfigs() {
        return this.dataSourceConfigs;
    }

    public String getDataSourceDriverClassName() {
        return dataSourceDriverClassName;
    }

    protected DataSourceConfig[] loadDataSourceConfig() {
        // fetch datasource configurations for all tenants from somewhere in the ether.
        return new DataSourceConfig[]{
                new DataSourceConfig(dataSourceUrl, new String[]{
                        "default"
                }, dataSourceUsername, dataSourcePassword, dataConnectionParms)
        };
    }

}
