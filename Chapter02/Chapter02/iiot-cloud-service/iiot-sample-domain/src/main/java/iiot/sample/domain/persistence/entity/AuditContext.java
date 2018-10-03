package iiot.sample.domain.persistence.entity;

/**
 * Created by 212568770 on 3/30/17.
 */

import lombok.extern.slf4j.Slf4j;
import org.eclipse.persistence.config.DescriptorCustomizer;
import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.descriptors.DescriptorEvent;
import org.eclipse.persistence.descriptors.DescriptorEventAdapter;
import org.eclipse.persistence.sessions.Session;

import java.util.Calendar;
import java.util.List;

@Slf4j
public class AuditContext extends DescriptorEventAdapter implements SessionCustomizer, DescriptorCustomizer {
    private static final ThreadLocal<AuditContext> threadServiceContext = new ThreadLocal<>();
    private String userName;
    private String tenantUuid;
    private static AuditContext getContext() {

        return threadServiceContext.get();
    }
    public static String getUserName(){
        return getContext().userName;
    }

    public static void initContext(String userName, String tenantUuid) {
        AuditContext auditContext = getContext();
        if (auditContext == null) {
            auditContext = new AuditContext();
            threadServiceContext.set(auditContext);
        }
        auditContext.userName = userName;
        auditContext.tenantUuid = tenantUuid;
    }

    public static String getTenantUuid(){
        if (getContext() == null){
            return "default";
        }
        return getContext().tenantUuid;
    }

    /** This will audit a specific class. */
    public void customize(ClassDescriptor descriptor) {
        descriptor.getEventManager().addListener(this);
    }

    /** This will audit all classes. */
    public void customize(Session session) {
        session.getLogin().setQueryRetryAttemptCount(0);
        // session.getLogin().setTableQualifier(DbUtil.getSchemaName(AuditContext.getTenantUuid()));
        for (ClassDescriptor descriptor : session.getDescriptors().values()) {
            customize(descriptor);
        }
    }

    @Override
    public void aboutToInsert(DescriptorEvent event) {
        for (String table : (List<String>)event.getDescriptor().getTableNames()) {
            log.debug("table  %s %s %s ", table, AuditContext.getUserName(), AuditContext.getTenantUuid());
            event.getRecord().put(table + ".created_by", AuditContext.getUserName());
            event.getRecord().put(table + ".created_date", Calendar.getInstance());
            event.getRecord().put(table + ".updated_by", AuditContext.getUserName());
            event.getRecord().put(table + ".updated_date", Calendar.getInstance());
            event.getRecord().put(table + ".tenant_uuid",  AuditContext.getTenantUuid());
        }
    }

    @Override
    public void aboutToUpdate(DescriptorEvent event) {
        for (String table : (List<String>)event.getDescriptor().getTableNames()) {
            log.debug("table  %s %s %s ", table, AuditContext.getUserName(), AuditContext.getTenantUuid());
            event.getRecord().put(table + ".updated_by", AuditContext.getUserName());
            event.getRecord().put(table + ".updated_date", Calendar.getInstance());
        }
    }

}
