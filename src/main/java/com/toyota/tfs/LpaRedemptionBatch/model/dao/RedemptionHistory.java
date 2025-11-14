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
@Table(name = "lpa_redemption_history", schema = "lpa")
public class RedemptionHistory {
    @Id
    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "grounding_date")
    private String groundingDate;

    @Column(name = "return_date")
    private String returnDate;

    @Column(name = "primary_borrower_ssntin")
    private String primaryBorrowerSsntin;

    @Column(name = "co_borrower_ssn")
    private String coBorrowerSsn;

    @Column(name = "dealer_purchase_type")
    private String dealerPurchaseType;

    @Column(name = "dealer_purchase_date")
    private String dealerPurchaseDate;

    @Column(name = "transportation_status")
    private String transportationStatus;

    @Column(name = "program_name")
    private String programName;

    @Column(name = "offer_name")
    private String offerName;

    @Column(name = "offer_start_date")
    private String offerStartDate;

    @Column(name = "offer_end_date")
    private String offerEndDate;

    @Column(name = "lpa_program_id")
    private String lpaProgramId;

    @Column(name = "redemption_rejection_reason")
    private String redemptionRejectionReason;

    @Column(name = "maximum_no_of_payments_to_waive")
    private String maximumNoOfPaymentsToWaive;

    @Column(name = "maximum_amount_to_waive")
    private String maximumAmountToWaive;

    @Column(name = "buydown_id")
    private String buydownId;

    @Column(name = "disposition_fee_waiver_reason")
    private String dispositionFeeWaiverReason;

    @Column(name = "eligible_for_redemption")
    private String eligibleForRedemption;

    @Column(name = "check_in_daily_batch_job")
    private String checkInDailyBatchJob;

    @Column(name = "dealer_purchase_pass_complete")
    private String dealerPurchasePassComplete;

    @Column(name = "redemption_process_complete")
    private String redemptionProcessComplete;

    @Column(name = "new_account_number")
    private String newAccountNumber;

    @Column(name = "user_id")
    private String userId;
    
    @Column(name = "status")
    private String status;

    @Column(name = "day_of_batch_run")
    private int dayOfBatchRun;

    @Column(name = "record_insert_date")
    private String recordInsertDate;

    @Column(name = "record_update_date")
    private String recordUpdateDate;
    
    @Column(name = "lpa_offer_present")
    private String lpaOfferPresent;
    
    @Column(name = "lpa_offer_eligible")
    private String lpaOfferEligible;
    
    @Column(name = "dispofee_waiver_reason_sent")
    private String dispofeeWaiverReasonSent;
    
    @Column(name = "new_dispo_account_number")
    private String newDispoAccountNumber;
    
    @Column(name = "lpa_redemption_decision_date")
    private String lpaRedemptionDecisionDate;
    
    @Column(name = "dispo_sent_date")
    private String dispoSentDate;

}
