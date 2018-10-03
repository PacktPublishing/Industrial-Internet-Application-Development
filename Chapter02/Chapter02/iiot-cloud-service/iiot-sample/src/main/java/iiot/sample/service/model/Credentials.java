package iiot.sample.service.model;

import iiot.sample.domain.persistence.entity.AuditContext;
import iiot.sample.utilities.BasicAuthDecodeUtil;
import iiot.sample.utilities.OAuthTokenUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;


public class Credentials {
    @Getter
    private final String authorization;

    @Getter
    private final String tenantUuid;

    @Getter
    private final String username;

    @Setter
    @Getter
    private String referer;

    public Credentials(String authorization, String tenantUuid) {
        this.authorization = authorization;
        this.tenantUuid = tenantUuid;

        /*Map<String, Object> authParse = OAuthTokenUtil.parseToken(authorization);
        String uname = (String) authParse.get("user_name");
        this.username = (uname == null) ? "clientCrendentials" : uname;
        this.email = (String) authParse.get("email");
        initAuditContext(username, tenantUuid);*/

        Map<String,String> basicAuthParse = BasicAuthDecodeUtil.decodeBasicAuth(authorization);
        String uname=basicAuthParse.get(BasicAuthDecodeUtil.USERNAME);
        this.username = (uname == null) ? "clientCrendentials" : uname;
        initAuditContext(username, tenantUuid);


    }



    public static void initAudit(String authorization, String tenantUuid){
       Credentials credentials =  new Credentials(authorization, tenantUuid);
        initAuditContext(credentials.username, credentials.tenantUuid);
    }

    private static void initAuditContext(String username, String tenantUuid){
        AuditContext.initContext(username, tenantUuid);

    }

}
