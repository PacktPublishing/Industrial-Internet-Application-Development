package iiot.sample.utilities;

import iiot.sample.config.TenantDataSourceConfig;
import iiot.sample.domain.persistence.entity.DbUtil;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.database.OfflineConnection;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import liquibase.resource.ResourceAccessor;
import liquibase.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by ashayasotornrat on 4/12/17.
 */
@Slf4j
public class SmarterSpringLiquibase extends SpringLiquibase {
    private final TenantDataSourceConfig tenantDataSourceConfig;

    public SmarterSpringLiquibase(TenantDataSourceConfig tenantDataSourceConfig) {
        this.tenantDataSourceConfig = tenantDataSourceConfig;

    }

    @Override
    public void afterPropertiesSet() throws LiquibaseException {
        DataSourceConfig[] dataSourceConfigs = tenantDataSourceConfig.getDataSourceConfigs();

        Connection dataSourceConnection = null;
        for (DataSourceConfig dataSourceConfig : dataSourceConfigs) {
            try {
                log.info(String.format("dataSourceConfig %s ", dataSourceConfig));
                dataSourceConnection = DriverManager.getConnection(dataSourceConfig.getDataSourceUri(), dataSourceConfig.getUserName(), dataSourceConfig.getPassword());
                log.info(String.format("got Connection %s ", dataSourceConfig));
                log.info(String.format("dataSourceConfig %s ", dataSourceConfig));
                for (String tenantUuid : dataSourceConfig.getTenants()) {
                    performLiquibaseOperations(dataSourceConnection, dataSourceConfig, tenantUuid);
                }
            } catch (Exception e) {
                log.error("", e);
            } finally {
                DbUtil.closeQuietly(dataSourceConnection);
            }
        }


    }

    private void performLiquibaseOperations(Connection dataSourceConnection, DataSourceConfig dataSourceConfig, String tenantUuid) {
        Connection liquiConnection = null;
        Liquibase liquibase = null;
        try {
            createSchemaIfNotPresent(dataSourceConnection, tenantUuid);
            final String liquiConnectionUrl = dataSourceConfig.getDataSourceUri();
            log.info("Got liquiConnectionUrl " + liquiConnectionUrl);
            liquiConnection = DriverManager.getConnection(liquiConnectionUrl, dataSourceConfig.getUserName(), dataSourceConfig.getPassword());
            liquiConnection.setSchema(DbUtil.getSchemaName(tenantUuid));
            log.info("Got successful liqui connection");

            liquibase = super.createLiquibase(liquiConnection);
            log.error("Smarter Db Schema name {}" ,DbUtil.getSchemaName(tenantUuid));
            liquibase.getDatabase().setDefaultSchemaName(DbUtil.getSchemaName(tenantUuid));

            performUpdate(liquibase);
            liquiConnection.close();
        } catch (Exception e) {
            log.error("", e);
        } finally {
            closeQuietly(liquibase);
            DbUtil.closeQuietly(liquiConnection);
        }
    }

    private void closeQuietly(Liquibase liquibase) {
        if (liquibase != null) {
            Database database = liquibase.getDatabase();
            if (database != null) {
                try {
                    database.close();
                } catch (Exception e) {
                    log.warn("", e);
                }
            }
        }
    }

    protected void createSchemaIfNotPresent(Connection connection, String tenantUuid) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(connection, false));
            jdbcTemplate.execute(String.format("CREATE SCHEMA IF NOT EXISTS %s", DbUtil.getSchemaName(tenantUuid)));
            jdbcTemplate.execute("commit");
        } finally {
            DbUtil.closeQuietly(connection);
        }
    }

    @Override
    protected Database createDatabase(Connection c, ResourceAccessor resourceAccessor) throws DatabaseException {
        Object liquibaseConnection;
        if (c == null) {
            log.warn("Null connection returned by liquibase datasource. Using offline unknown database");
            liquibaseConnection = new OfflineConnection("offline:unknown", resourceAccessor);
        } else {
            liquibaseConnection = new JdbcConnection(c);
        }

        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation((DatabaseConnection) liquibaseConnection);
        if (StringUtils.trimToNull(this.defaultSchema) != null) {
            database.setDefaultSchemaName(this.defaultSchema);
        }

        return database;
    }

}
