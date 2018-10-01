package iiot.sample.domain.persistence.entity;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by 212568770 on 4/11/17.
 */
@Slf4j
public class DbUtil {


    public static final String getSchemaName(String tenantUuid){
        return String.format("iiot_sample%s", ("default".equals(tenantUuid) ? "" : "_" + tenantUuid.replaceAll("-","")));
    }

    public static final void closeQuietly(Connection connection){
        if (connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                log.info("failed to close connection", e);
            }
        }
    }

}
