package com.toyota.tfs.LpaRedemptionBatch.model.defi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DefiRequest {

	private TenantContext tenantContext;
	private ActivityContext activityContext;
	private String accountNumber;
	private String lpaPaymentWaiverRedemption;
	private String redemptionRejectionReason;
	private String maximumNumberOfPaymentsToWaive;
	private String maximumAmountToWaive;
	private String buyDownId;
	private String dispositionFeeWaiverReason;

}
