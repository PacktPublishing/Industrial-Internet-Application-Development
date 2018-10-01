package iiot.sample.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Created by 212568770 on 4/16/17.
 */
@Configuration
@Slf4j
public class WatchCloudConfig extends AbstractCloudConfig {
    @Autowired
    TenantDataSourceConfig tenantDataSourceConfig;

    @Bean
    public DataSource dataSource() {
        log.info("Using Cloud DataSource");
        return new SmarterRoutingDataSource(tenantDataSourceConfig);
    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
       log.info("using cloud entityManagerFactory " );
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPersistenceUnitName("iiotSamplePersistentUnit");
        return entityManagerFactoryBean;
    }

    @Bean
    JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
       log.info("Using CloundTransaction Manager " );
        return new JpaTransactionManager(entityManagerFactory);
    }


}
