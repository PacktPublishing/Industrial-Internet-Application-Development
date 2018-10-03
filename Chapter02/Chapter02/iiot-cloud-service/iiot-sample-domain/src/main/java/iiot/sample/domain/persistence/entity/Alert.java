package iiot.sample.domain.persistence.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.persistence.annotations.Mutable;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@EntityListeners({AuditContext.class})
@Data
@Table(name = "Alerts")
@Cacheable(value=false)
@Slf4j
public class Alert implements Serializable {
    @Id
    @Column(name = "ID", nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    @JsonProperty
    @Column(name = "alerts_uuid")
    private String alertsUuid;


    @Column(name = "severity")
    private int severity;

    @Column(name = "alert_name")
    private String alertName;


    @Column(name = "alert_info")
    private String alertInfo;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "created_by")
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "updated_by")
    private String updatedBy;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "tenant_uuid")
    private String tenantUuid;





}
