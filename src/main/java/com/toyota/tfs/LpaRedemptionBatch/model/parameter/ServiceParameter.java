package com.toyota.tfs.LpaRedemptionBatch.model.parameter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "service_parameter", schema = "public")
public class ServiceParameter {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "tenant_id")
    private String tenantId;
    
    @Column(name = "service_name")
    private String serviceName;
    
    @Column(name = "key")
    private String key;
    
    @Column(name = "value")
    private String value;
    
    @Column(name = "active_flag")
    private String activeFlag;

    @Column(name = "updated_by")
    private String updatedBy;
    
    @Column(name = "record_update_date")
    private String recordUpdateDate;

}
