package com.toyota.tfs.LpaRedemptionBatch.model.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tenant_details", schema = "public")
public class TenantDetails {
    @Id
    private String id;

    @Column(name = "tenant_id")
    private String tenantId;
    
    @Column(name = "defi_tenant_id")
    private String defiTenantId;

    @Column(name = "defi_client_id")
    private String defiClientId;

    @Column(name = "sparc_system_id")
    private String sparcSystemId;

}
