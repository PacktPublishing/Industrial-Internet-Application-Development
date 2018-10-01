--SET search_path = apm_watch_management;
INSERT INTO alerts (
    alerts_uuid,
    severity,
    alert_name,
    alert_info,
    created_by,
    updated_by,
    tenant_uuid
)
VALUES
    ('alerts1d',1,'Jet Turbine SystemAlert','Jet Turbine Alerts issue','System','System','SingleTenant'),
     ('alerts2d',2,'Power Turbine SystemAlert','Power Turbine Alerts issue','System','System','SingleTenant');

