package com.toyota.tfs.LpaRedemptionBatch.process;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.toyota.tfs.LpaRedemptionBatch.model.dao.RedemptionHistory;
import com.toyota.tfs.LpaRedemptionBatch.model.parameter.ServiceParameter;
import com.toyota.tfs.LpaRedemptionBatch.service.LpaAggregatorService;
import com.toyota.tfs.LpaRedemptionBatch.service.ProcessLPARequest;
import com.toyota.tfs.LpaRedemptionBatch.service.ServiceParamterService;

@ExtendWith(MockitoExtension.class)
class AccountProcessorTest {

    @Mock
    private LpaAggregatorService lpaAggregatorService;

    @Mock
    private ProcessLPARequest processLPARequest;

    @Mock
    private ServiceParamterService serviceParamterService;

    @InjectMocks
    private AccountProcessor accountProcessor;

    private RedemptionHistory redemptionHistory;
    private List<ServiceParameter> serviceParameterList;

    @BeforeEach
    void setUp() {
        redemptionHistory = new RedemptionHistory();
        redemptionHistory.setAccountNumber("12345");

        serviceParameterList = new ArrayList<>();
        ServiceParameter parameter = new ServiceParameter();
        serviceParameterList.add(parameter);
    }

    @Test
    void testFetchServiceParameters() throws Exception {
        when(serviceParamterService.getRecord()).thenReturn(serviceParameterList);

        accountProcessor.fetchServiceParameters();

        verify(serviceParamterService, times(1)).getRecord();
    }

    @Test
    void testProcess() throws Exception {
        RedemptionHistory processedRedemptionHistory = new RedemptionHistory();
        processedRedemptionHistory.setAccountNumber("12345");

        // Ensure the service parameters are fetched correctly
        when(serviceParamterService.getRecord()).thenReturn(serviceParameterList);

        // Use doReturn().when() to avoid strict stubbing issues
        doReturn(processedRedemptionHistory).when(lpaAggregatorService).process(redemptionHistory, serviceParameterList);

        accountProcessor.fetchServiceParameters(); // Ensure service parameters are fetched
        RedemptionHistory result = accountProcessor.process(redemptionHistory);

        assertEquals("12345", result.getAccountNumber());
        verify(lpaAggregatorService, times(1)).process(redemptionHistory, serviceParameterList);
        verify(processLPARequest, times(1)).saveGroundingRequest(processedRedemptionHistory);
    }
}