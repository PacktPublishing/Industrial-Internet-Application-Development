package iiot.sample.config;

import iiot.sample.domain.persistence.entity.AuditContext;
import iiot.sample.domain.persistence.entity.DbUtil;
import iiot.sample.utilities.DataConnectionParms;
import iiot.sample.utilities.DataSourceConfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 212568770 on 4/16/17.
 */
@Slf4j
public class SmarterRoutingDataSource extends AbstractRoutingDataSource{

    private  Map<String, TransactionAwareDataSourceProxy> dbUrlProxies = new HashMap<>();
    private final TenantDataSourceConfig tenantDataSourceConfig;

    public SmarterRoutingDataSource(TenantDataSourceConfig tenantDataSourceConfig){
        this.tenantDataSourceConfig = tenantDataSourceConfig;
    }

    @Override
    public DataSource determineTargetDataSource() {
        String tenantUuid = (String) this.determineCurrentLookupKey();
        log.debug(String.format("Determining DataSource for tenant  %s", tenantUuid));

        DataSourceConfig dataSourceConfig = tenantDataSourceConfig.getDataSourceConfig(tenantUuid);
        DataConnectionParms dataConnectionParms = dataSourceConfig.getDataConnectionParms();
                TransactionAwareDataSourceProxy proxy = dbUrlProxies.get(dataSourceConfig.getDataSourceUri());
        if (proxy == null){
            synchronized (SmarterRoutingDataSource.class){
                if (dbUrlProxies.get(dataSourceConfig.getDataSourceUri()) == null){
                    org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();

                    dataSource.setDriverClassName(tenantDataSourceConfig.getDataSourceDriverClassName());
                    dataSource.setUrl(dataSourceConfig.getDataSourceUri());
                    dataSource.setUsername(dataSourceConfig.getUserName());
                    dataSource.setPassword(dataSourceConfig.getPassword());
                    dataSource.setDefaultAutoCommit(false);
                    dataSource.setUseDisposableConnectionFacade(true);
                    dataSource.setJmxEnabled(true);
                    dataSource.setTestWhileIdle(false);
                    dataSource.setTestOnBorrow(true);
                    dataSource.setTestOnReturn(false);
                    dataSource.setValidationInterval(dataConnectionParms.getValidationInterval());
                    dataSource.setValidationQuery("SELECT 1");
                    dataSource.setTimeBetweenEvictionRunsMillis(dataConnectionParms.getTimeBetweenEvictionRunMillis());
                    dataSource.setMaxActive(dataConnectionParms.getMaxActive());
                    dataSource.setInitialSize(dataConnectionParms.getInitialSize());
                    dataSource.setTestWhileIdle(true);
                    dataSource.setMaxWait(dataConnectionParms.getMaxWait());
                    dataSource.setRemoveAbandonedTimeout(dataConnectionParms.getRemoveAbandonedTimeout());
                    dataSource.setRemoveAbandoned(true);
                    dataSource.setMinEvictableIdleTimeMillis(dataConnectionParms.getMinEvictableIdleTimeMillis());
                    dataSource.setMaxIdle(dataConnectionParms.getMaxIdle());
                    dataSource.setMinIdle(dataConnectionParms.getMinIdle());
                    dataSource.setLogAbandoned(true);
                    dataSource.setRemoveAbandoned(true);
                    dataSource.setJdbcInterceptors(
                            "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
                                    "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer;"+
                                    "org.apache.tomcat.jdbc.pool.interceptor.ResetAbandonedTimer;"+
                                    "org.apache.tomcat.jdbc.pool.interceptor.SlowQueryReport");
                    proxy = new TransactionAwareDataSourceProxy(dataSource);
                    dbUrlProxies.put(dataSourceConfig.getDataSourceUri(), proxy);
                }
            }
        }
        return proxy;
    }



    @Override
    public Connection getConnection() throws SQLException {
        java.sql.Connection  connection =  this.determineTargetDataSource().getConnection();
        String tenantUuid = "default";
        log.debug(String.format("Switching Connection Schema to  %s schemaName %s ", tenantUuid, DbUtil.getSchemaName(tenantUuid)));
        if (!DbUtil.getSchemaName(tenantUuid).equals(connection.getSchema())){
            connection.setSchema(DbUtil.getSchemaName(tenantUuid));
        }

        return connection;
    }



    @Override
    public void afterPropertiesSet() {
        // do nothing on purpose
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return AuditContext.getTenantUuid();
    }



}
