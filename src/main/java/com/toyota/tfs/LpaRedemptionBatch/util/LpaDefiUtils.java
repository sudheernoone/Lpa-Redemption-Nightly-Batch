package com.toyota.tfs.LpaRedemptionBatch.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.toyota.tfs.LpaRedemptionBatch.model.dao.RedemptionHistory;
import com.toyota.tfs.LpaRedemptionBatch.model.dao.TenantDetails;
import com.toyota.tfs.LpaRedemptionBatch.model.defi.ActivityContext;
import com.toyota.tfs.LpaRedemptionBatch.model.defi.DefiRequest;
import com.toyota.tfs.LpaRedemptionBatch.model.defi.TenantContext;
import com.toyota.tfs.LpaRedemptionBatch.repository.TenantDetailsRepository;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Getter
@Setter
public class LpaDefiUtils {

    @Value("${api.tfs.defi.util.consumerSystemId}")
    private String defiApiConsumerSystemId;
    @Value("${api.tfs.defi.util.consumerEnvironmentId}")
    private String defiApiConsumerEnvironmentId;
    @Value("${api.tfs.defi.util.userId}")
    private String defiApiUserId;
    @Value("${api.tfs.defi.util.activityId}")
    private String defiApiActivityId;
    
    @Autowired
    TenantDetailsRepository tenantDetailsRepository;

    private TenantContext prepareTenantContext(String tenantId) {
        TenantContext tenantContext = new TenantContext();
        TenantDetails tenantDetails = tenantDetailsRepository.getTenantDetails(tenantId);
        
        tenantContext.setConsumerSystemId(defiApiConsumerSystemId);
        tenantContext.setConsumerEnvironmentId(defiApiConsumerEnvironmentId);
        tenantContext.setClientId(tenantDetails.getDefiClientId());
        tenantContext.setTenantId(tenantDetails.getDefiTenantId());

        return tenantContext;
    }

    private ActivityContext prepareActivityContext() {
        ActivityContext activityContext = new ActivityContext();
        activityContext.setUserId(defiApiUserId);
        activityContext.setActivityId(defiApiActivityId);
        return activityContext;
    }


    public DefiRequest prepareDefiRequest(RedemptionHistory redemptionHistory) throws Exception {
		DefiRequest defiRequest = new DefiRequest();
		defiRequest.setTenantContext(prepareTenantContext(redemptionHistory.getTenantId()));
		defiRequest.setActivityContext(prepareActivityContext());
		defiRequest.setAccountNumber(redemptionHistory.getAccountNumber());

		if(redemptionHistory.getEligibleForRedemption()!=null)
			defiRequest.setLpaPaymentWaiverRedemption(redemptionHistory.getEligibleForRedemption());
		if(redemptionHistory.getRedemptionRejectionReason()!=null)
			defiRequest.setRedemptionRejectionReason(redemptionHistory.getRedemptionRejectionReason());
		if(redemptionHistory.getMaximumNoOfPaymentsToWaive()!=null)
			defiRequest.setMaximumNumberOfPaymentsToWaive(redemptionHistory.getMaximumNoOfPaymentsToWaive());
		if(redemptionHistory.getMaximumAmountToWaive()!=null)
			defiRequest.setMaximumAmountToWaive(redemptionHistory.getMaximumAmountToWaive());
		if(redemptionHistory.getBuydownId()!=null)
			defiRequest.setBuyDownId(redemptionHistory.getBuydownId());
		if(redemptionHistory.getDispositionFeeWaiverReason()!=null && redemptionHistory.getDispofeeWaiverReasonSent()==null)
		  defiRequest.setDispositionFeeWaiverReason(redemptionHistory.getDispositionFeeWaiverReason());

        return defiRequest;

    }
}
