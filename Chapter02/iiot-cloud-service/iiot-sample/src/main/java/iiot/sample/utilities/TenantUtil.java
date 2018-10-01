package iiot.sample.utilities;


import org.springframework.stereotype.Component;

/**
 * Created by 212421703.
 */
@Component
public class TenantUtil {

    private String injectedTenantUuid;

    public void injectTenantUuid(String injectedTenantUuid){
        this.injectedTenantUuid = injectedTenantUuid;
    }

    /**
     * if a tenantUuid is set using injectTenantUuid(..) then return that else return tenantUuid from TenantContext
     * the class encapsulates TenantContext.getInstance() which is difficult to mock
     * @return tenantUuid from TenantContext
     */
    public  String getTenantUuid() {
        if (injectedTenantUuid != null){
            return injectedTenantUuid;
        }
        return "SingleTenant";
    }
}
