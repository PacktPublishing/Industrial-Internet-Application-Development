package iiot.sample;


import iiot.sample.config.TenantDataSourceConfig;
import iiot.sample.utilities.SmarterSpringLiquibase;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfiguration {
    private final DataSource dataSource;
    @Value("${spring.datasource.driver-class-name:org.postgresql.Driver}")
    private String dataSourceDriverClassName;

    @Value("${vcap.services.${iiot_sample_postgres_name:iiot-sample-postgres}.credentials.uri}")
    private String dataSourceUrl;

    @Value("${vcap.services.${iiot_sample_postgres_name:iiot-sample-postgres}.credentials.username}")
    private String dataSourceUsername;

    @Value("${vcap.services.${iiot_sample_postgres_name:iiot-sample-postgres}.credentials.password}")
    private String dataSourcePassword;
    @Autowired
    public LiquibaseConfiguration(DataSource dataSource){
        this.dataSource = dataSource;

    }

    @Bean
    public SpringLiquibase liquibase(TenantDataSourceConfig tenantDataSourceConfig) {
        SmarterSpringLiquibase liquibase = new SmarterSpringLiquibase(tenantDataSourceConfig);
        liquibase.setChangeLog("classpath:db/changelog.xml");
        liquibase.setDataSource(dataSource);
        liquibase.setDefaultSchema("iiot-sample");
        liquibase.setDropFirst(false);
        liquibase.setShouldRun(true);
        return liquibase;
    }
}
