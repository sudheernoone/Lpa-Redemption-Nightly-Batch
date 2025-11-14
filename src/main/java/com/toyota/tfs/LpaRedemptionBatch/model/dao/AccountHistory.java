package com.toyota.tfs.LpaRedemptionBatch.model.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lpa_account_history", schema = "lpa")
public class AccountHistory {
    @Id
    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "contract_booking_date")
    private String contractBookingDate;

    @Column(name = "contract_start_date")
    private String contractStartDate;
    
    @Column(name = "product_type")
    private String productType;
    
    @Column(name = "fin_program_type")
    private String finProgramType;
    
    @Column(name = "tier")
    private String tier;
    
    @Column(name = "group_area_id")
    private String groupAreaId;

    @Column(name = "primary_borrower_ssntin")
    private String primaryBorrowerSsntin;

    @Column(name = "co_borrower_ssn")
    private String coBorrowerSsn;
    
    @Column(name = "vin")
    private String vin;
    
    @Column(name = "vehicle_make")
    private String vehicleMake;
    
    @Column(name = "vehicle_model_code")
    private String vehicleModelCode;
    
    @Column(name = "vehicle_model_year")
    private String vehicleModelYear;
    
    @Column(name = "vehicle_condition")
    private String vehicleCondition;
    
    @Column(name = "dealer_number")
    private String dealerNumber;
    
    @Column(name = "dealer_purchase_type")
    private String dealerPurchaseType;

    @Column(name = "dealer_purchase_date")
    private String dealerPurchaseDate;
    
   @Column(name = "original_maturity_date")
    private String originalMaturityDate;
    
    @Column(name = "current_maturity_date")
    private String currentMaturityDate;
    
    @Column(name = "original_term")
    private String originalTerm;
    
    @Column(name = "extended_term")
    private String extendedTerm;
    
    @Column(name = "current_term")
    private String currentTerm;
    
    @Column(name = "no_of_payments_remaining")
    private String noOfPaymentsRemaining;

    @Column(name = "transportation_status")
    private String transportationStatus;
    
    @Column(name = "transportation_issue_date")
    private String transportationIssueDate;

}
