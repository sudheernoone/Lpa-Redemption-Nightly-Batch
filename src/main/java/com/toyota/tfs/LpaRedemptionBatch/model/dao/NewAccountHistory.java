package com.toyota.tfs.LpaRedemptionBatch.model.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "lpa_new_account_history", schema = "lpa")
public class NewAccountHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "tenant_id")
    private String tenantId;
    
    @Column(name = "grounded_account_number")
    private String groundedAccountNumber;

    @Column(name = "new_account_number")
    private String newAccountNumber;

    @Column(name = "sparc_call_flag")
    private String sparcValidationFlag;

    @Column(name = "redemption_eligible_flag")
    private String redemptionEligibleFlag;

    @Column(name = "record_insert_date")
    private String recordInsertDate;
    
    @Column(name = "record_update_date")
    private String recordUpdateDate;

}
