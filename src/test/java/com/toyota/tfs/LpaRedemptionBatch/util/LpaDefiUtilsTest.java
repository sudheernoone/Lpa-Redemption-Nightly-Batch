package com.toyota.tfs.LpaRedemptionBatch.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.toyota.tfs.LpaRedemptionBatch.model.dao.RedemptionHistory;
import com.toyota.tfs.LpaRedemptionBatch.model.dao.TenantDetails;
import com.toyota.tfs.LpaRedemptionBatch.model.defi.DefiRequest;
import com.toyota.tfs.LpaRedemptionBatch.repository.TenantDetailsRepository;

@ExtendWith(MockitoExtension.class)
class LpaDefiUtilsTest {

    @Mock
    private TenantDetailsRepository tenantDetailsRepository;

    @InjectMocks
    private LpaDefiUtils lpaDefiUtils;

    @BeforeEach
    void setUp() {
        // Set up mock values for @Value fields
        lpaDefiUtils.setDefiApiConsumerSystemId("testSystemId");
        lpaDefiUtils.setDefiApiConsumerEnvironmentId("testEnvironmentId");
        lpaDefiUtils.setDefiApiUserId("testUserId");
        lpaDefiUtils.setDefiApiActivityId("testActivityId");
    }

    @Test
    void testPrepareDefiRequest() throws Exception {
        // Arrange
        RedemptionHistory redemptionHistory = new RedemptionHistory();
        redemptionHistory.setTenantId("tenant123");
        redemptionHistory.setAccountNumber("account123");
        redemptionHistory.setEligibleForRedemption("Y");
        redemptionHistory.setRedemptionRejectionReason("reason");
        redemptionHistory.setMaximumNoOfPaymentsToWaive(5+"");
        redemptionHistory.setMaximumAmountToWaive(1000.0+"");
        redemptionHistory.setBuydownId("buydown123");
        redemptionHistory.setDispositionFeeWaiverReason("feeReason");

        TenantDetails tenantDetails = new TenantDetails();
        tenantDetails.setDefiClientId("client123");
        tenantDetails.setDefiTenantId("tenant123");

        when(tenantDetailsRepository.getTenantDetails("tenant123")).thenReturn(tenantDetails);

        // Act
        DefiRequest defiRequest = lpaDefiUtils.prepareDefiRequest(redemptionHistory);

        // Assert
        assertNotNull(defiRequest);
        assertEquals("account123", defiRequest.getAccountNumber());
        assertEquals("testSystemId", defiRequest.getTenantContext().getConsumerSystemId());
        assertEquals("testEnvironmentId", defiRequest.getTenantContext().getConsumerEnvironmentId());
        assertEquals("client123", defiRequest.getTenantContext().getClientId());
        assertEquals("tenant123", defiRequest.getTenantContext().getTenantId());
        assertEquals("testUserId", defiRequest.getActivityContext().getUserId());
        assertEquals("testActivityId", defiRequest.getActivityContext().getActivityId());
        assertEquals("Y",defiRequest.getLpaPaymentWaiverRedemption());
        assertEquals("reason", defiRequest.getRedemptionRejectionReason());
        assertEquals(5+"", defiRequest.getMaximumNumberOfPaymentsToWaive());
        assertEquals(1000.0+"", defiRequest.getMaximumAmountToWaive());
        assertEquals("buydown123", defiRequest.getBuyDownId());
        assertEquals("feeReason", defiRequest.getDispositionFeeWaiverReason());

        verify(tenantDetailsRepository, times(1)).getTenantDetails("tenant123");
    }
}