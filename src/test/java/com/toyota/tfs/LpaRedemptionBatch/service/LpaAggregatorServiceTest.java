package com.toyota.tfs.LpaRedemptionBatch.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.toyota.tfs.LpaRedemptionBatch.model.ccp.CcpResponse;
import com.toyota.tfs.LpaRedemptionBatch.model.ccp.Offer;
import com.toyota.tfs.LpaRedemptionBatch.model.dao.AccountHistory;
import com.toyota.tfs.LpaRedemptionBatch.model.dao.NewAccountHistory;
import com.toyota.tfs.LpaRedemptionBatch.model.dao.RedemptionHistory;
import com.toyota.tfs.LpaRedemptionBatch.model.defi.DefiResponse;
import com.toyota.tfs.LpaRedemptionBatch.model.parameter.ServiceParameter;
import com.toyota.tfs.LpaRedemptionBatch.model.sparc.LpaQualifiers;
import com.toyota.tfs.LpaRedemptionBatch.model.sparc.Program;
import com.toyota.tfs.LpaRedemptionBatch.model.sparc.SparcResponse;
import com.toyota.tfs.LpaRedemptionBatch.util.DateUtils;
import com.toyota.tfs.LpaRedemptionBatch.util.ErrorMessageHandler;

@ExtendWith(MockitoExtension.class)
class LpaAggregatorServiceTest {

    @Mock
    DefiSaveLPAService defiSaveLPAService;
    @Mock
    private ProcessLPARequest processLPARequest;

    @Mock
    private SparcLpaEligibilityService sparcLpaEligibilityService;

    @Mock
    private CcpOfferService ccpOfferService;

    @Mock
    private RedemptionErrorService redemptionErrorService;

    @Mock
    ErrorMessageHandler errorMessageHandler;

    @Mock
    private DateUtils dateUtils;

    @Mock
    private MongoService mongoService;


    @Mock
    private ServiceParamterService serviceParamterService;


    @InjectMocks
    private LpaAggregatorService lpaAggregatorService;

    private RedemptionHistory redemptionHistory;
    private List<ServiceParameter> serviceParameterList;


    @BeforeEach
    void setUp() {
        redemptionHistory = new RedemptionHistory();
        redemptionHistory.setAccountNumber("12345");
        redemptionHistory.setDayOfBatchRun(1);

        serviceParameterList = new ArrayList<>();
        ServiceParameter serviceParameter = new ServiceParameter();
        serviceParameter.setKey("testKey");
        serviceParameter.setValue("testValue");
        serviceParameterList.add(serviceParameter);
    }

    @Test
    void testProcess_AH_NUll() throws Exception {
        // Arrange
        RedemptionHistory processedRedemptionHistory = new RedemptionHistory();
        processedRedemptionHistory.setReturnDate("03-10-2023");
        processedRedemptionHistory.setDayOfBatchRun(2);
        processedRedemptionHistory.setCheckInDailyBatchJob("Y");
        processedRedemptionHistory.setRedemptionProcessComplete("N");

        // Ensure service parameters are set correctly
        serviceParameterList.get(0).setKey("testKey");
        serviceParameterList.get(0).setValue("testValue");

        // Use doReturn().when() for stubbing
        doReturn("30").when(serviceParamterService).getRecordsByTenantIdAndKey(anyList(), any(), any());
        when(processLPARequest.isReturnDateValid(any(), any())).thenReturn(true);
        when(dateUtils.getDateTime()).thenReturn("2023-10-01");

        // Act
        RedemptionHistory result = lpaAggregatorService.process(processedRedemptionHistory, serviceParameterList);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getDayOfBatchRun());
        assertEquals("Y", result.getCheckInDailyBatchJob());
        assertEquals("N", result.getRedemptionProcessComplete());
    }

    @Test
    void testProcess_PBCB_Null() throws Exception {
        // Arrange
        RedemptionHistory processedRedemptionHistory = new RedemptionHistory();
        processedRedemptionHistory.setAccountNumber("0500012345");
        processedRedemptionHistory.setTenantId("t002");
        processedRedemptionHistory.setReturnDate("03-10-2023");
        processedRedemptionHistory.setDayOfBatchRun(2);
        processedRedemptionHistory.setCheckInDailyBatchJob("Y");
        processedRedemptionHistory.setRedemptionProcessComplete("N");

        // Ensure service parameters are set correctly
        serviceParameterList.get(0).setKey("testKey");
        serviceParameterList.get(0).setValue("testValue");

        // Use doReturn().when() for stubbing
        doReturn("30").when(serviceParamterService).getRecordsByTenantIdAndKey(anyList(), any(), any());
        when(processLPARequest.isReturnDateValid(any(), any())).thenReturn(true);
        when(dateUtils.getDateTime()).thenReturn("2023-10-01");

        // Update stubbing to handle dynamic arguments
        // Update stubbing to handle null or dynamic arguments
        when(processLPARequest.getAccountHistory("0500012345", "t002"))
                .thenReturn(new AccountHistory("0500012345", "t002", "2023-01-01", "2024-01-01", "M", "O", null, null, null, null, null, null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null, null));
        // Act
        RedemptionHistory result = lpaAggregatorService.process(processedRedemptionHistory, serviceParameterList);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getDayOfBatchRun());
        assertEquals("Y", result.getCheckInDailyBatchJob());
        assertEquals("N", result.getRedemptionProcessComplete());
    }

    @Test
    void testProcess_PBCB_Null_AH_NotNull() throws Exception {
        // Arrange
        RedemptionHistory processedRedemptionHistory = new RedemptionHistory();
        processedRedemptionHistory.setAccountNumber("0500012345");
        processedRedemptionHistory.setTenantId("t002");
        processedRedemptionHistory.setReturnDate("03-10-2023");
        processedRedemptionHistory.setDayOfBatchRun(2);
        processedRedemptionHistory.setCheckInDailyBatchJob("Y");
        processedRedemptionHistory.setRedemptionProcessComplete("N");

        // Ensure service parameters are set correctly
        serviceParameterList.get(0).setKey("testKey");
        serviceParameterList.get(0).setValue("testValue");

        // Use doReturn().when() for stubbing
        doReturn("30").when(serviceParamterService).getRecordsByTenantIdAndKey(anyList(), any(), any());
        when(processLPARequest.isReturnDateValid(any(), any())).thenReturn(true);
        when(dateUtils.getDateTime()).thenReturn("2023-10-01");

        // Update stubbing to handle dynamic arguments
        // Update stubbing to handle null or dynamic arguments
        when(processLPARequest.getAccountHistory("0500012345", "t002"))
                .thenReturn(new AccountHistory("0500012345", "t002", "2023-01-01", "2024-01-01", "M", "O", null, null, "1213443", "323232", null, null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null, null));
        // Act
        RedemptionHistory result = lpaAggregatorService.process(processedRedemptionHistory, serviceParameterList);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getDayOfBatchRun());
        assertEquals("Y", result.getCheckInDailyBatchJob());
        assertEquals("N", result.getRedemptionProcessComplete());
    }

    @Test
    void testProcess_CCPCallFail() throws Exception {
        // Arrange
        RedemptionHistory processedRedemptionHistory = new RedemptionHistory();
        processedRedemptionHistory.setAccountNumber("0500012345");
        processedRedemptionHistory.setTenantId("t002");
        processedRedemptionHistory.setPrimaryBorrowerSsntin("67890988");
        processedRedemptionHistory.setCoBorrowerSsn("123456789");
        processedRedemptionHistory.setReturnDate("03-10-2023");
        processedRedemptionHistory.setDayOfBatchRun(2);
        processedRedemptionHistory.setCheckInDailyBatchJob("Y");
        processedRedemptionHistory.setRedemptionProcessComplete("N");

        // Ensure service parameters are set correctly
        serviceParameterList.get(0).setKey("testKey");
        serviceParameterList.get(0).setValue("testValue");

        // Use doReturn().when() for stubbing
        doReturn("30").when(serviceParamterService).getRecordsByTenantIdAndKey(anyList(), any(), any());
        when(processLPARequest.isReturnDateValid(any(), any())).thenReturn(true);
        when(dateUtils.getDateTime()).thenReturn("2023-10-01");

        // Update stubbing to handle dynamic arguments
        // Update stubbing to handle null or dynamic arguments
        when(processLPARequest.getAccountHistory("0500012345", "t002"))
                .thenReturn(new AccountHistory("0500012345", "t002", "2023-01-01", "2024-01-01", "M", "O", null, null, null, null, null, null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null, null));
        // Act
        RedemptionHistory result = lpaAggregatorService.process(processedRedemptionHistory, serviceParameterList);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getDayOfBatchRun());
        assertEquals("Y", result.getCheckInDailyBatchJob());
        assertEquals("N", result.getRedemptionProcessComplete());
    }

    @Test
    void testProcess_InvalidReturnDate_LpaWaiverInEligible() throws Exception {
        // Arrange
        RedemptionHistory processedRedemptionHistory = new RedemptionHistory();
        processedRedemptionHistory.setAccountNumber("0500012345");
        processedRedemptionHistory.setTenantId("t002");
        processedRedemptionHistory.setPrimaryBorrowerSsntin("67890988");
        processedRedemptionHistory.setCoBorrowerSsn("123456789");
        processedRedemptionHistory.setReturnDate("03-10-2023");
        processedRedemptionHistory.setDayOfBatchRun(2);
        processedRedemptionHistory.setCheckInDailyBatchJob("Y");
        processedRedemptionHistory.setRedemptionProcessComplete("N");

        // Ensure service parameters are set correctly
        serviceParameterList.get(0).setKey("testKey");
        serviceParameterList.get(0).setValue("testValue");

        // Use doReturn().when() for stubbing
        doReturn("30").when(serviceParamterService).getRecordsByTenantIdAndKey(anyList(), any(), any());
        when(processLPARequest.isReturnDateValid(any(), any())).thenReturn(false);
        when(dateUtils.getDateTime()).thenReturn("2023-10-01");

        // Update stubbing to handle dynamic arguments
        // Update stubbing to handle null or dynamic arguments
        when(processLPARequest.getAccountHistory("0500012345", "t002"))
                .thenReturn(new AccountHistory("0500012345", "t002", "2023-01-01", "2024-01-01", "M", "O", null, null, null, null, null, null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null, null));
        CcpResponse ccpResponse = new CcpResponse();
        Offer offer = new Offer();
        offer.setProgramName("Test Program");
        when(ccpOfferService.getOfferDetails("0500012345", "t002")).thenReturn(ccpResponse);
        when(processLPARequest.extractValidCcpOffer(ccpResponse)).thenReturn(offer);
        // Act
        RedemptionHistory result = lpaAggregatorService.process(processedRedemptionHistory, serviceParameterList);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getDayOfBatchRun());
        assertEquals("N", result.getCheckInDailyBatchJob());
        assertEquals("Y", result.getRedemptionProcessComplete());
    }

    @Test
    void testProcess_InvalidReturnDate_LpaWaiverEligible_DealerPurchasePassPresent() throws Exception {
        // Arrange
        RedemptionHistory processedRedemptionHistory = new RedemptionHistory();
        processedRedemptionHistory.setAccountNumber("0500012345");
        processedRedemptionHistory.setTenantId("t002");
        processedRedemptionHistory.setPrimaryBorrowerSsntin("67890988");
        processedRedemptionHistory.setCoBorrowerSsn("123456789");
        processedRedemptionHistory.setReturnDate("03-10-2023");
        processedRedemptionHistory.setDayOfBatchRun(2);
        processedRedemptionHistory.setCheckInDailyBatchJob("Y");
        processedRedemptionHistory.setRedemptionProcessComplete("N");

        // Ensure service parameters are set correctly
        serviceParameterList.get(0).setKey("testKey");
        serviceParameterList.get(0).setValue("testValue");

        // Use doReturn().when() for stubbing
        doReturn("30").when(serviceParamterService).getRecordsByTenantIdAndKey(anyList(), any(), any());
        when(processLPARequest.isReturnDateValid(any(), any())).thenReturn(false);
        when(dateUtils.getDateTime()).thenReturn("2023-10-01");

        // Update stubbing to handle dynamic arguments
        // Update stubbing to handle null or dynamic arguments
        when(processLPARequest.getAccountHistory("0500012345", "t002"))
                .thenReturn(new AccountHistory("0500012345", "t002", "2023-01-01", "2024-01-01", "M", "O", null, null, null, null, null, null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null, null));
        CcpResponse ccpResponse = new CcpResponse();
        Offer offer = new Offer();
        offer.setProgramName("Test Program");
        when(ccpOfferService.getOfferDetails("0500012345", "t002")).thenReturn(ccpResponse);
        when(processLPARequest.extractValidCcpOffer(ccpResponse)).thenReturn(offer);
        when(processLPARequest.isLpaWaiverEligible(processedRedemptionHistory)).thenReturn(true);
        when(processLPARequest.checkDealerPurchasePass(processedRedemptionHistory)).thenReturn(true);
        // Act
        RedemptionHistory result = lpaAggregatorService.process(processedRedemptionHistory, serviceParameterList);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getDayOfBatchRun());
        assertEquals("Y", result.getCheckInDailyBatchJob());
        assertEquals("N", result.getRedemptionProcessComplete());
    }
    @Test
    void testProcess_InvalidReturnDate_LpaWaiverEligible_DealerPurchasePassNotPresent() throws Exception {
        // Arrange
        RedemptionHistory processedRedemptionHistory = new RedemptionHistory();
        processedRedemptionHistory.setAccountNumber("0500012345");
        processedRedemptionHistory.setTenantId("t002");
        processedRedemptionHistory.setPrimaryBorrowerSsntin("67890988");
        processedRedemptionHistory.setCoBorrowerSsn("123456789");
        processedRedemptionHistory.setReturnDate("03-10-2023");
        processedRedemptionHistory.setDayOfBatchRun(2);
        processedRedemptionHistory.setCheckInDailyBatchJob("Y");
        processedRedemptionHistory.setRedemptionProcessComplete("N");

        serviceParameterList.get(0).setKey("testKey");
        serviceParameterList.get(0).setValue("testValue");

        // Use lenient stubbing if necessary
        lenient().doReturn("30").when(serviceParamterService).getRecordsByTenantIdAndKey(anyList(), any(), any());
        when(processLPARequest.isReturnDateValid(any(), any())).thenReturn(false);
        when(dateUtils.getDateTime()).thenReturn("2023-10-01");

        AccountHistory accountHistory = new AccountHistory("0500012345", "t002", "2023-01-01", "2024-01-01", "M", "O", null, null, null, null, null, null, null, null, null,
                null, "S", null, null, null, null, null, null, null, null, null);

        when(processLPARequest.getAccountHistory("0500012345", "t002")).thenReturn(accountHistory);
        CcpResponse ccpResponse = new CcpResponse();
        Offer offer = new Offer();
        offer.setProgramName("Test Program");
        when(ccpOfferService.getOfferDetails("0500012345", "t002")).thenReturn(ccpResponse);
        when(processLPARequest.extractValidCcpOffer(ccpResponse)).thenReturn(offer);
        when(processLPARequest.isLpaWaiverEligible(processedRedemptionHistory)).thenReturn(true);
        when(processLPARequest.checkDealerPurchasePass(processedRedemptionHistory)).thenReturn(false);
        when(processLPARequest.checkDealerInfoFromAccountHistory(accountHistory)).thenReturn(true);

        // Act
        RedemptionHistory result = lpaAggregatorService.process(processedRedemptionHistory, serviceParameterList);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getDayOfBatchRun());
        assertEquals("Y", result.getCheckInDailyBatchJob());
        assertEquals("N", result.getRedemptionProcessComplete());
    }

    @Test
    void testProcess_LpaWaiverEligible_DealerPurchasePresent_PR() throws Exception {
        // Arrange
        RedemptionHistory processedRedemptionHistory = new RedemptionHistory();
        processedRedemptionHistory.setAccountNumber("0500012345");
        processedRedemptionHistory.setTenantId("t002");
        processedRedemptionHistory.setPrimaryBorrowerSsntin("67890988");
        processedRedemptionHistory.setCoBorrowerSsn("123456789");
        processedRedemptionHistory.setReturnDate("03-10-2023");
        processedRedemptionHistory.setDayOfBatchRun(2);
        processedRedemptionHistory.setCheckInDailyBatchJob("Y");
        processedRedemptionHistory.setRedemptionProcessComplete("N");

        serviceParameterList.get(0).setKey("testKey");
        serviceParameterList.get(0).setValue("testValue");

        // Use lenient stubbing if necessary
        lenient().doReturn("30").when(serviceParamterService).getRecordsByTenantIdAndKey(anyList(), any(), any());
        when(processLPARequest.isReturnDateValid(any(), any())).thenReturn(false);
        when(dateUtils.getDateTime()).thenReturn("2023-10-01");

        AccountHistory accountHistory = new AccountHistory("0500012345", "t002", "2023-01-01", "2024-01-01", "M", "O", null, null, null, null, null, null, null, null, null,
                null, "P", null, null, null, null, null, null, null, null, null);

        when(processLPARequest.getAccountHistory("0500012345", "t002")).thenReturn(accountHistory);
        CcpResponse ccpResponse = new CcpResponse();
        Offer offer = new Offer();
        offer.setProgramName("Test Program");
        when(ccpOfferService.getOfferDetails("0500012345", "t002")).thenReturn(ccpResponse);
        when(processLPARequest.extractValidCcpOffer(ccpResponse)).thenReturn(offer);
        when(processLPARequest.isLpaWaiverEligible(processedRedemptionHistory)).thenReturn(true);
        when(processLPARequest.checkDealerPurchasePass(processedRedemptionHistory)).thenReturn(false);
        when(processLPARequest.isAutomatedGrounding(processedRedemptionHistory)).thenReturn(true);

        // Act
        RedemptionHistory result = lpaAggregatorService.process(processedRedemptionHistory, serviceParameterList);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getDayOfBatchRun());
        assertEquals("Y", result.getCheckInDailyBatchJob());
        assertEquals("N", result.getRedemptionProcessComplete());
    }

    @Test
    void testProcess_LpaWaiverInEligible_DealerPurchaseNotPresent_PR() throws Exception {
        // Arrange
        RedemptionHistory processedRedemptionHistory = new RedemptionHistory();
        processedRedemptionHistory.setAccountNumber("0500012345");
        processedRedemptionHistory.setTenantId("t002");
        processedRedemptionHistory.setPrimaryBorrowerSsntin("67890988");
        processedRedemptionHistory.setCoBorrowerSsn("123456789");
        processedRedemptionHistory.setReturnDate("03-10-2023");
        processedRedemptionHistory.setDealerPurchaseType("P");
        processedRedemptionHistory.setDayOfBatchRun(2);
        processedRedemptionHistory.setCheckInDailyBatchJob("Y");
        processedRedemptionHistory.setRedemptionProcessComplete("N");
        processedRedemptionHistory.setDispositionFeeWaiverReason(null);

        serviceParameterList.get(0).setKey("testKey");
        serviceParameterList.get(0).setValue("testValue");

        // Use lenient stubbing if necessary
        lenient().doReturn("30").when(serviceParamterService).getRecordsByTenantIdAndKey(anyList(), any(), any());
        when(processLPARequest.isReturnDateValid(any(), any())).thenReturn(true);
        when(dateUtils.getDateTime()).thenReturn("2023-10-01");

        AccountHistory accountHistory = new AccountHistory("0500012345", "t002", "2023-01-01", "2024-01-01", "M", "O", null, null, null, null, null, null, null, null, null,
                null, "P", null, null, null, null, null, null, null, null, null);

        when(processLPARequest.getAccountHistory("0500012345", "t002")).thenReturn(accountHistory);
        CcpResponse ccpResponse = new CcpResponse();
        Offer offer = new Offer();
        offer.setProgramName("Test Program");
        when(ccpOfferService.getOfferDetails("0500012345", "t002")).thenReturn(ccpResponse);
        when(processLPARequest.extractValidCcpOffer(ccpResponse)).thenReturn(offer);
        when(processLPARequest.isLpaWaiverEligible(processedRedemptionHistory)).thenReturn(true);
        when(processLPARequest.checkDealerPurchasePass(processedRedemptionHistory)).thenReturn(false);
        when(processLPARequest.checkDealerInfoFromAccountHistory(accountHistory)).thenReturn(true);
        lenient().when(processLPARequest.isIneligibileDealerPurchaseType(processedRedemptionHistory)).thenReturn(true);

        when(processLPARequest.isReturnDateWithinLpaOffer(processedRedemptionHistory)).thenReturn(true);

        // Act
        RedemptionHistory result = lpaAggregatorService.process(processedRedemptionHistory, serviceParameterList);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getDayOfBatchRun());
        assertEquals("Y", result.getCheckInDailyBatchJob());
        assertEquals("N", result.getRedemptionProcessComplete());
    }

    @Test
    void testProcess_ReturnDateNotWithinLPAOffer() throws Exception {
        // Arrange
        RedemptionHistory processedRedemptionHistory = new RedemptionHistory();
        processedRedemptionHistory.setAccountNumber("0500012345");
        processedRedemptionHistory.setTenantId("t002");
        processedRedemptionHistory.setPrimaryBorrowerSsntin("67890988");
        processedRedemptionHistory.setCoBorrowerSsn("123456789");
        processedRedemptionHistory.setReturnDate("03-10-2023");
        processedRedemptionHistory.setDayOfBatchRun(2);
        processedRedemptionHistory.setCheckInDailyBatchJob("Y");
        processedRedemptionHistory.setRedemptionProcessComplete("N");
        processedRedemptionHistory.setDispofeeWaiverReasonSent("Y");

        serviceParameterList.get(0).setKey("testKey");
        serviceParameterList.get(0).setValue("testValue");

        // Use lenient stubbing if necessary
        lenient().doReturn("30").when(serviceParamterService).getRecordsByTenantIdAndKey(anyList(), any(), any());
        when(processLPARequest.isReturnDateValid(any(), any())).thenReturn(true);
        when(dateUtils.getDateTime()).thenReturn("2023-10-01");

        AccountHistory accountHistory = new AccountHistory("0500012345", "t002", "2023-01-01", "2024-01-01", "M", "O", null, null, null, null, null, null, null, null, null,
                null, "S", null, null, null, null, null, null, null, null, null);

        when(processLPARequest.getAccountHistory("0500012345", "t002")).thenReturn(accountHistory);
        CcpResponse ccpResponse = new CcpResponse();
        Offer offer = new Offer();
        offer.setProgramName("Test Program");
        when(ccpOfferService.getOfferDetails("0500012345", "t002")).thenReturn(ccpResponse);
        when(processLPARequest.extractValidCcpOffer(ccpResponse)).thenReturn(offer);
        when(processLPARequest.isLpaWaiverEligible(processedRedemptionHistory)).thenReturn(true);
        when(processLPARequest.isReturnDateWithinLpaOffer(processedRedemptionHistory)).thenReturn(false);

        // Act
        RedemptionHistory result = lpaAggregatorService.process(processedRedemptionHistory, serviceParameterList);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getDayOfBatchRun());
        assertEquals("Y", result.getCheckInDailyBatchJob());
        assertEquals("N", result.getRedemptionProcessComplete());
    }

    @Test
    void testProcess_AutomatedGroundingWithinLPAOffer_DealerInfoNotPresent() throws Exception {
        // Arrange
        RedemptionHistory processedRedemptionHistory = new RedemptionHistory();
        processedRedemptionHistory.setAccountNumber("0500012345");
        processedRedemptionHistory.setTenantId("t002");
        processedRedemptionHistory.setPrimaryBorrowerSsntin("67890988");
        processedRedemptionHistory.setCoBorrowerSsn("123456789");
        processedRedemptionHistory.setReturnDate("03-10-2023");
        processedRedemptionHistory.setDayOfBatchRun(2);
        processedRedemptionHistory.setCheckInDailyBatchJob("Y");
        processedRedemptionHistory.setRedemptionProcessComplete("N");
        processedRedemptionHistory.setDispofeeWaiverReasonSent("Y");

        serviceParameterList.get(0).setKey("testKey");
        serviceParameterList.get(0).setValue("testValue");

        // Use lenient stubbing if necessary
        lenient().doReturn("30").when(serviceParamterService).getRecordsByTenantIdAndKey(anyList(), any(), any());
        when(processLPARequest.isReturnDateValid(any(), any())).thenReturn(true);
        when(dateUtils.getDateTime()).thenReturn("2023-10-01");

        AccountHistory accountHistory = new AccountHistory("0500012345", "t002", "2023-01-01", "2024-01-01", "M", "O", null, null, null, null, null, null, null, null, null,
                null, "S", null, null, null, null, null, null, null, null, null);

        when(processLPARequest.getAccountHistory("0500012345", "t002")).thenReturn(accountHistory);
        CcpResponse ccpResponse = new CcpResponse();
        Offer offer = new Offer();
        offer.setProgramName("Test Program");
        when(ccpOfferService.getOfferDetails("0500012345", "t002")).thenReturn(ccpResponse);
        when(processLPARequest.extractValidCcpOffer(ccpResponse)).thenReturn(offer);
        when(processLPARequest.isLpaWaiverEligible(processedRedemptionHistory)).thenReturn(true);
        when(processLPARequest.isReturnDateWithinLpaOffer(processedRedemptionHistory)).thenReturn(true);
        when(processLPARequest.isAutomatedGrounding(processedRedemptionHistory)).thenReturn(true);

        // Act
        RedemptionHistory result = lpaAggregatorService.process(processedRedemptionHistory, serviceParameterList);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getDayOfBatchRun());
        assertEquals("Y", result.getCheckInDailyBatchJob());
        assertEquals("N", result.getRedemptionProcessComplete());
    }
    @Test
    void testProcess_AutomatedGroundingWithinLPAOffer_DealerInfoNotPresent_TakefromAH() throws Exception {
        // Arrange
        RedemptionHistory processedRedemptionHistory = new RedemptionHistory();
        processedRedemptionHistory.setAccountNumber("0500012345");
        processedRedemptionHistory.setTenantId("t002");
        processedRedemptionHistory.setPrimaryBorrowerSsntin("67890988");
        processedRedemptionHistory.setCoBorrowerSsn("123456789");
        processedRedemptionHistory.setReturnDate("03-10-2023");
        processedRedemptionHistory.setDayOfBatchRun(2);
        processedRedemptionHistory.setCheckInDailyBatchJob("Y");
        processedRedemptionHistory.setRedemptionProcessComplete("N");
        processedRedemptionHistory.setDispofeeWaiverReasonSent("Y");

        serviceParameterList.get(0).setKey("testKey");
        serviceParameterList.get(0).setValue("testValue");

        // Use lenient stubbing if necessary
        lenient().doReturn("30").when(serviceParamterService).getRecordsByTenantIdAndKey(anyList(), any(), any());
        when(processLPARequest.isReturnDateValid(any(), any())).thenReturn(true);
        when(dateUtils.getDateTime()).thenReturn("2023-10-01");

        AccountHistory accountHistory = new AccountHistory("0500012345", "t002", "2023-01-01", "2024-01-01", "M", "O", null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, "O", null);

        when(processLPARequest.getAccountHistory("0500012345", "t002")).thenReturn(accountHistory);
        CcpResponse ccpResponse = new CcpResponse();
        Offer offer = new Offer();
        offer.setProgramName("Test Program");
        when(ccpOfferService.getOfferDetails("0500012345", "t002")).thenReturn(ccpResponse);
        when(processLPARequest.extractValidCcpOffer(ccpResponse)).thenReturn(offer);
        when(processLPARequest.isLpaWaiverEligible(processedRedemptionHistory)).thenReturn(true);
        when(processLPARequest.isReturnDateWithinLpaOffer(processedRedemptionHistory)).thenReturn(true);
        when(processLPARequest
                .checkDealerInfoFromAccountHistory(accountHistory)).thenReturn(true);
        when(processLPARequest.isAutomatedGrounding(processedRedemptionHistory)).thenReturn(true);

        // Act
        RedemptionHistory result = lpaAggregatorService.process(processedRedemptionHistory, serviceParameterList);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getDayOfBatchRun());
        assertEquals("Y", result.getCheckInDailyBatchJob());
        assertEquals("N", result.getRedemptionProcessComplete());
    }

    @Test
    void testProcess_AutomatedGroundingWithinLPAOffer_DealerInfoPresent() throws Exception {
        // Arrange
        RedemptionHistory processedRedemptionHistory = new RedemptionHistory();
        processedRedemptionHistory.setAccountNumber("0500012345");
        processedRedemptionHistory.setTenantId("t002");
        processedRedemptionHistory.setPrimaryBorrowerSsntin("67890988");
        processedRedemptionHistory.setCoBorrowerSsn("123456789");
        processedRedemptionHistory.setReturnDate("03-10-2023");
        processedRedemptionHistory.setDayOfBatchRun(2);
        processedRedemptionHistory.setCheckInDailyBatchJob("Y");
        processedRedemptionHistory.setRedemptionProcessComplete("N");
        processedRedemptionHistory.setDispofeeWaiverReasonSent("Y");

        serviceParameterList.get(0).setKey("testKey");
        serviceParameterList.get(0).setValue("testValue");

        // Use lenient stubbing if necessary
        lenient().doReturn("30").when(serviceParamterService).getRecordsByTenantIdAndKey(anyList(), any(), any());
        when(processLPARequest.isReturnDateValid(any(), any())).thenReturn(true);
        when(dateUtils.getDateTime()).thenReturn("2023-10-01");

        AccountHistory accountHistory = new AccountHistory("0500012345", "t002", "2023-01-01", "2024-01-01", "M", "O", null, null, null, null, null, null, null, null, null,
                null, "S", null, null, null, null, null, null, null, null, null);

        when(processLPARequest.getAccountHistory("0500012345", "t002")).thenReturn(accountHistory);
        CcpResponse ccpResponse = new CcpResponse();
        Offer offer = new Offer();
        offer.setProgramName("Test Program");
        when(ccpOfferService.getOfferDetails("0500012345", "t002")).thenReturn(ccpResponse);
        when(processLPARequest.extractValidCcpOffer(ccpResponse)).thenReturn(offer);
        when(processLPARequest.isLpaWaiverEligible(processedRedemptionHistory)).thenReturn(true);
        when(processLPARequest.isReturnDateWithinLpaOffer(processedRedemptionHistory)).thenReturn(true);
        when(processLPARequest.checkDealerPurchasePass(processedRedemptionHistory)).thenReturn(true);
        when(processLPARequest.isAutomatedGrounding(processedRedemptionHistory)).thenReturn(true);

        // Act
        RedemptionHistory result = lpaAggregatorService.process(processedRedemptionHistory, serviceParameterList);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getDayOfBatchRun());
        assertEquals("Y", result.getCheckInDailyBatchJob());
        assertEquals("N", result.getRedemptionProcessComplete());
    }

    @Test
    void testProcess_AutomatedGrounding_NewContractPresent_SparcFailed() throws Exception {
        // Arrange
        RedemptionHistory processedRedemptionHistory = new RedemptionHistory();
        processedRedemptionHistory.setAccountNumber("0500012345");
        processedRedemptionHistory.setTenantId("t002");
        processedRedemptionHistory.setPrimaryBorrowerSsntin("67890988");
        processedRedemptionHistory.setCoBorrowerSsn("123456789");
        processedRedemptionHistory.setReturnDate("03-10-2023");
        processedRedemptionHistory.setDayOfBatchRun(2);
        processedRedemptionHistory.setCheckInDailyBatchJob("Y");
        processedRedemptionHistory.setRedemptionProcessComplete("N");
        processedRedemptionHistory.setDispofeeWaiverReasonSent("Y");

        serviceParameterList.get(0).setKey("testKey");
        serviceParameterList.get(0).setValue("testValue");

        // Use lenient stubbing if necessary
        lenient().doReturn("30").when(serviceParamterService).getRecordsByTenantIdAndKey(anyList(), any(), any());
        when(processLPARequest.isReturnDateValid(any(), any())).thenReturn(true);
        when(dateUtils.getDateTime()).thenReturn("2023-10-01");

        AccountHistory accountHistory = new AccountHistory("0500012345", "t002", "2023-01-01", "2024-01-01", "M", "O", null, null, null, null, null, null, null, null, null,
                null, "S", null, null, null, null, null, null, null, null, null);

        when(processLPARequest.getAccountHistory("0500012345", "t002")).thenReturn(accountHistory);
        CcpResponse ccpResponse = new CcpResponse();
        Offer offer = new Offer();
        offer.setProgramName("Test Program");
        when(ccpOfferService.getOfferDetails("0500012345", "t002")).thenReturn(ccpResponse);
        when(processLPARequest.extractValidCcpOffer(ccpResponse)).thenReturn(offer);

        List<AccountHistory> listOfNewContract=new ArrayList<>();
        AccountHistory accountHistory1 = new AccountHistory("0500012346", "t002", "2023-01-01", "2024-01-01", "M", "O", null, null, null, null, null, null, null, null, null,
                null, "S", null, null, null, null, null, null, null, null, null);

        AccountHistory accountHistory2 = new AccountHistory("0500012347", "t002", "2023-01-01", "2024-01-01", "M", "O", null, null, null, null, null, null, null, null, null,
                null, "S", null, null, null, null, null, null, null, null, null);
        listOfNewContract.add(accountHistory1);
        listOfNewContract.add(accountHistory2);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date actualReturnDate = dateFormat.parse("03-10-2023");
        when(processLPARequest.getNewContract(any(), anyString(), anyString())).thenReturn(listOfNewContract);
        lenient().when(lpaAggregatorService.checkForNewContract(processedRedemptionHistory,actualReturnDate,60)).thenReturn(listOfNewContract);


        when(processLPARequest.isLpaWaiverEligible(processedRedemptionHistory)).thenReturn(true);
        when(processLPARequest.isReturnDateWithinLpaOffer(processedRedemptionHistory)).thenReturn(true);
        when(processLPARequest.checkDealerPurchasePass(processedRedemptionHistory)).thenReturn(true);
        when(processLPARequest.isAutomatedGrounding(processedRedemptionHistory)).thenReturn(true);

        // Act
        RedemptionHistory result = lpaAggregatorService.process(processedRedemptionHistory, serviceParameterList);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getDayOfBatchRun());
        assertEquals("Y", result.getCheckInDailyBatchJob());
        assertEquals("N", result.getRedemptionProcessComplete());
    }

    @Test
    void testProcess_AutomatedGrounding_NewContractPresent_SparcSuccess_NoProgram() throws Exception {
        // Arrange
        RedemptionHistory processedRedemptionHistory = new RedemptionHistory();
        processedRedemptionHistory.setAccountNumber("0500012345");
        processedRedemptionHistory.setTenantId("t002");
        processedRedemptionHistory.setPrimaryBorrowerSsntin("67890988");
        processedRedemptionHistory.setCoBorrowerSsn("123456789");
        processedRedemptionHistory.setReturnDate("03-10-2023");
        processedRedemptionHistory.setDayOfBatchRun(2);
        processedRedemptionHistory.setCheckInDailyBatchJob("Y");
        processedRedemptionHistory.setRedemptionProcessComplete("N");
        processedRedemptionHistory.setDispofeeWaiverReasonSent("Y");

        serviceParameterList.get(0).setKey("testKey");
        serviceParameterList.get(0).setValue("testValue");

        // Use lenient stubbing if necessary
        lenient().doReturn("30").when(serviceParamterService).getRecordsByTenantIdAndKey(anyList(), any(), any());
        when(processLPARequest.isReturnDateValid(any(), any())).thenReturn(true);
        when(dateUtils.getDateTime()).thenReturn("2023-10-01");

        AccountHistory accountHistory = new AccountHistory("0500012345", "t002", "2023-01-01", "2024-01-01", "M", "O", null, null, null, null, null, null, null, null, null,
                null, "S", null, null, null, null, null, null, null, null, null);

        when(processLPARequest.getAccountHistory("0500012345", "t002")).thenReturn(accountHistory);
        CcpResponse ccpResponse = new CcpResponse();
        Offer offer = new Offer();
        offer.setProgramName("Test Program");
        when(ccpOfferService.getOfferDetails("0500012345", "t002")).thenReturn(ccpResponse);
        when(processLPARequest.extractValidCcpOffer(ccpResponse)).thenReturn(offer);

        List<AccountHistory> listOfNewContract=new ArrayList<>();
        AccountHistory accountHistory1 = new AccountHistory("0500012346", "t002", "2023-01-01", "2024-01-01", "M", "O", null, null, null, null, null, null, null, null, null,
                null, "S", null, null, null, null, null, null, null, null, null);

        AccountHistory accountHistory2 = new AccountHistory("0500012347", "t002", "2023-01-01", "2024-01-01", "M", "O", null, null, null, null, null, null, null, null, null,
                null, "S", null, null, null, null, null, null, null, null, null);
        listOfNewContract.add(accountHistory1);
        listOfNewContract.add(accountHistory2);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date actualReturnDate = dateFormat.parse("03-10-2023");
        when(processLPARequest.getNewContract(any(), anyString(), anyString())).thenReturn(listOfNewContract);
        lenient().when(lpaAggregatorService.checkForNewContract(processedRedemptionHistory,actualReturnDate,60)).thenReturn(listOfNewContract);


        when(processLPARequest.isLpaWaiverEligible(processedRedemptionHistory)).thenReturn(true);
        when(processLPARequest.isReturnDateWithinLpaOffer(processedRedemptionHistory)).thenReturn(true);
        when(processLPARequest.checkDealerPurchasePass(processedRedemptionHistory)).thenReturn(true);
        when(processLPARequest.isAutomatedGrounding(processedRedemptionHistory)).thenReturn(true);

        List<LpaQualifiers> lpaQualifiersList=new ArrayList<>();
        LpaQualifiers lpaQualifiers=new LpaQualifiers();

        lpaQualifiersList.add(lpaQualifiers);
        SparcResponse sparcResponse=new SparcResponse();
        sparcResponse.setClientId("TestClient");
        sparcResponse.setRequestType("TestType");
        sparcResponse.setSenderId("TestSender");
        sparcResponse.setTenantId("t002");
        sparcResponse.setLpaQualifiers(lpaQualifiersList);

        NewAccountHistory newAccountHistory=new NewAccountHistory();
        newAccountHistory.setNewAccountNumber(accountHistory1.getAccountNumber());

        lpaAggregatorService.tenantId="t002";
        when(lpaAggregatorService.callSparc(accountHistory,accountHistory1,processedRedemptionHistory)).thenReturn(sparcResponse);
        when(sparcLpaEligibilityService.getLpaEligibility(accountHistory,accountHistory1,processedRedemptionHistory,"t002")).thenReturn(sparcResponse);
        when( processLPARequest
                .getNewAccountByGroundedAccountNumerAndNewAccountNumber(processedRedemptionHistory.getAccountNumber(),
                        accountHistory1.getAccountNumber(), processedRedemptionHistory.getTenantId())).thenReturn(newAccountHistory);
        // Act
        RedemptionHistory result = lpaAggregatorService.process(processedRedemptionHistory, serviceParameterList);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getDayOfBatchRun());
        assertEquals("Y", result.getCheckInDailyBatchJob());
        assertEquals("N", result.getRedemptionProcessComplete());
    }
    @Test
    void testProcess_AutomatedGrounding_NewContractPresent_SparcSuccess_DefiCallFailed() throws Exception {
        // Arrange
        RedemptionHistory processedRedemptionHistory = new RedemptionHistory();
        processedRedemptionHistory.setAccountNumber("0500012345");
        processedRedemptionHistory.setTenantId("t002");
        processedRedemptionHistory.setPrimaryBorrowerSsntin("67890988");
        processedRedemptionHistory.setCoBorrowerSsn("123456789");
        processedRedemptionHistory.setReturnDate("03-10-2023");
        processedRedemptionHistory.setDayOfBatchRun(2);
        processedRedemptionHistory.setCheckInDailyBatchJob("Y");
        processedRedemptionHistory.setRedemptionProcessComplete("N");
        processedRedemptionHistory.setDispofeeWaiverReasonSent("Y");

        serviceParameterList.get(0).setKey("testKey");
        serviceParameterList.get(0).setValue("testValue");

        // Use lenient stubbing if necessary
        lenient().doReturn("30").when(serviceParamterService).getRecordsByTenantIdAndKey(anyList(), any(), any());
        when(processLPARequest.isReturnDateValid(any(), any())).thenReturn(true);
        when(dateUtils.getDateTime()).thenReturn("2023-10-01");

        AccountHistory accountHistory = new AccountHistory("0500012345", "t002", "2023-01-01", "2024-01-01", "M", "O", null, null, null, null, null, null, null, null, null,
                null, "S", null, null, null, null, null, null, null, null, null);

        when(processLPARequest.getAccountHistory("0500012345", "t002")).thenReturn(accountHistory);
        CcpResponse ccpResponse = new CcpResponse();
        Offer offer = new Offer();
        offer.setProgramName("Test Program");
        when(ccpOfferService.getOfferDetails("0500012345", "t002")).thenReturn(ccpResponse);
        when(processLPARequest.extractValidCcpOffer(ccpResponse)).thenReturn(offer);

        List<AccountHistory> listOfNewContract=new ArrayList<>();
        AccountHistory accountHistory1 = new AccountHistory("0500012346", "t002", "2023-01-01", "2024-01-01", "M", "O", null, null, null, null, null, null, null, null, null,
                null, "S", null, null, null, null, null, null, null, null, null);

        AccountHistory accountHistory2 = new AccountHistory("0500012347", "t002", "2023-01-01", "2024-01-01", "M", "O", null, null, null, null, null, null, null, null, null,
                null, "S", null, null, null, null, null, null, null, null, null);
        listOfNewContract.add(accountHistory1);
        listOfNewContract.add(accountHistory2);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date actualReturnDate = dateFormat.parse("03-10-2023");
        when(processLPARequest.getNewContract(any(), anyString(), anyString())).thenReturn(listOfNewContract);
        lenient().when(lpaAggregatorService.checkForNewContract(processedRedemptionHistory,actualReturnDate,60)).thenReturn(listOfNewContract);


        when(processLPARequest.isLpaWaiverEligible(processedRedemptionHistory)).thenReturn(true);
        when(processLPARequest.isReturnDateWithinLpaOffer(processedRedemptionHistory)).thenReturn(true);
        when(processLPARequest.checkDealerPurchasePass(processedRedemptionHistory)).thenReturn(true);
        when(processLPARequest.isAutomatedGrounding(processedRedemptionHistory)).thenReturn(true);

        List<LpaQualifiers> lpaQualifiersList=new ArrayList<>();
        LpaQualifiers lpaQualifiers=new LpaQualifiers();
        Program program=new Program();
        program.setId("P001");
        List<Program> programList=new ArrayList<>();
        programList.add(program);
        lpaQualifiers.setPrograms(programList);
        lpaQualifiersList.add(lpaQualifiers);
        SparcResponse sparcResponse=new SparcResponse();
        sparcResponse.setClientId("TestClient");
        sparcResponse.setRequestType("TestType");
        sparcResponse.setSenderId("TestSender");
        sparcResponse.setTenantId("t002");
        sparcResponse.setLpaQualifiers(lpaQualifiersList);

        NewAccountHistory newAccountHistory=new NewAccountHistory();
        newAccountHistory.setNewAccountNumber(accountHistory1.getAccountNumber());

        lpaAggregatorService.tenantId="t002";
        when(lpaAggregatorService.callSparc(accountHistory,accountHistory1,processedRedemptionHistory)).thenReturn(sparcResponse);
        when(sparcLpaEligibilityService.getLpaEligibility(accountHistory,accountHistory1,processedRedemptionHistory,"t002")).thenReturn(sparcResponse);
when( processLPARequest
                .getNewAccountByGroundedAccountNumerAndNewAccountNumber(processedRedemptionHistory.getAccountNumber(),
                       accountHistory1.getAccountNumber(), processedRedemptionHistory.getTenantId())).thenReturn(newAccountHistory);
        // Act
        RedemptionHistory result = lpaAggregatorService.process(processedRedemptionHistory, serviceParameterList);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getDayOfBatchRun());
        assertEquals("Y", result.getCheckInDailyBatchJob());
        assertEquals("N", result.getRedemptionProcessComplete());
    }

    @Test
    void testProcess_AutomatedGrounding_NewContractPresent_SparcSuccess_DefiCallSuccess() throws Exception {
        // Arrange
        RedemptionHistory processedRedemptionHistory = new RedemptionHistory();
        processedRedemptionHistory.setAccountNumber("0500012345");
        processedRedemptionHistory.setTenantId("t002");
        processedRedemptionHistory.setPrimaryBorrowerSsntin("67890988");
        processedRedemptionHistory.setCoBorrowerSsn("123456789");
        processedRedemptionHistory.setReturnDate("03-10-2023");
        processedRedemptionHistory.setDayOfBatchRun(2);
        processedRedemptionHistory.setCheckInDailyBatchJob("Y");
        processedRedemptionHistory.setRedemptionProcessComplete("N");
        processedRedemptionHistory.setDispofeeWaiverReasonSent(null);

        serviceParameterList.get(0).setKey("testKey");
        serviceParameterList.get(0).setValue("testValue");

        // Use lenient stubbing if necessary
        lenient().doReturn("30").when(serviceParamterService).getRecordsByTenantIdAndKey(anyList(), any(), any());
        when(processLPARequest.isReturnDateValid(any(), any())).thenReturn(true);
        when(dateUtils.getDateTime()).thenReturn("2023-10-01");

        AccountHistory accountHistory = new AccountHistory("0500012345", "t002", "2023-01-01", "2024-01-01", "M", "O", null, null, null, null, null, null, null, null, null,
                null, "S", null, null, null, null, null, null, null, null, null);

        when(processLPARequest.getAccountHistory("0500012345", "t002")).thenReturn(accountHistory);
        CcpResponse ccpResponse = new CcpResponse();
        Offer offer = new Offer();
        offer.setProgramName("Test Program");
        when(ccpOfferService.getOfferDetails("0500012345", "t002")).thenReturn(ccpResponse);
        when(processLPARequest.extractValidCcpOffer(ccpResponse)).thenReturn(offer);

        List<AccountHistory> listOfNewContract=new ArrayList<>();
        AccountHistory accountHistory1 = new AccountHistory("0500012346", "t002", "2023-01-01", "2024-01-01", "M", "O", null, null, null, null, null, null, null, null, null,
                null, "S", null, null, null, null, null, null, null, null, null);

        AccountHistory accountHistory2 = new AccountHistory("0500012347", "t002", "2023-01-01", "2024-01-01", "M", "O", null, null, null, null, null, null, null, null, null,
                null, "S", null, null, null, null, null, null, null, null, null);
        listOfNewContract.add(accountHistory1);
        listOfNewContract.add(accountHistory2);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date actualReturnDate = dateFormat.parse("03-10-2023");
        when(processLPARequest.getNewContract(any(), anyString(), anyString())).thenReturn(listOfNewContract);
        lenient().when(lpaAggregatorService.checkForNewContract(processedRedemptionHistory,actualReturnDate,60)).thenReturn(listOfNewContract);


        when(processLPARequest.isLpaWaiverEligible(processedRedemptionHistory)).thenReturn(true);
        when(processLPARequest.isReturnDateWithinLpaOffer(processedRedemptionHistory)).thenReturn(true);
        when(processLPARequest.checkDealerPurchasePass(processedRedemptionHistory)).thenReturn(true);
        when(processLPARequest.isAutomatedGrounding(processedRedemptionHistory)).thenReturn(true);

        List<LpaQualifiers> lpaQualifiersList=new ArrayList<>();
        LpaQualifiers lpaQualifiers=new LpaQualifiers();
        Program program=new Program();
        program.setId("P001");
        List<Program> programList=new ArrayList<>();
        programList.add(program);
        lpaQualifiers.setPrograms(programList);
        lpaQualifiersList.add(lpaQualifiers);
        SparcResponse sparcResponse=new SparcResponse();
        sparcResponse.setClientId("TestClient");
        sparcResponse.setRequestType("TestType");
        sparcResponse.setSenderId("TestSender");
        sparcResponse.setTenantId("t002");
        sparcResponse.setLpaQualifiers(lpaQualifiersList);

        NewAccountHistory newAccountHistory=new NewAccountHistory();
        newAccountHistory.setNewAccountNumber(accountHistory1.getAccountNumber());

        lpaAggregatorService.tenantId="t002";
        when(lpaAggregatorService.callSparc(accountHistory,accountHistory1,processedRedemptionHistory)).thenReturn(sparcResponse);
        when(sparcLpaEligibilityService.getLpaEligibility(accountHistory,accountHistory1,processedRedemptionHistory,"t002")).thenReturn(sparcResponse);
        when( processLPARequest
                .getNewAccountByGroundedAccountNumerAndNewAccountNumber(processedRedemptionHistory.getAccountNumber(),
                        accountHistory1.getAccountNumber(), processedRedemptionHistory.getTenantId())).thenReturn(newAccountHistory);
        when(defiSaveLPAService.processTransaction(processedRedemptionHistory,processedRedemptionHistory.getTenantId())).thenReturn(new DefiResponse());
        // Act
        RedemptionHistory result = lpaAggregatorService.process(processedRedemptionHistory, serviceParameterList);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getDayOfBatchRun());
        assertEquals("N", result.getCheckInDailyBatchJob());
        assertEquals("Y", result.getRedemptionProcessComplete());
    }

    @Test
    void testProcess_NoNewContractPresent() throws Exception {
        // Arrange
        RedemptionHistory processedRedemptionHistory = new RedemptionHistory();
        processedRedemptionHistory.setAccountNumber("0500012345");
        processedRedemptionHistory.setTenantId("t002");
        processedRedemptionHistory.setPrimaryBorrowerSsntin("67890988");
        processedRedemptionHistory.setCoBorrowerSsn("123456789");
        processedRedemptionHistory.setReturnDate("03-10-2023");
        processedRedemptionHistory.setDayOfBatchRun(2);
        processedRedemptionHistory.setCheckInDailyBatchJob("Y");
        processedRedemptionHistory.setRedemptionProcessComplete("N");

        // Ensure service parameters are set correctly
        serviceParameterList.get(0).setKey("testKey");
        serviceParameterList.get(0).setValue("testValue");

        // Use doReturn().when() for stubbing
        doReturn("30").when(serviceParamterService).getRecordsByTenantIdAndKey(anyList(), any(), any());
        when(processLPARequest.isReturnDateValid(any(), any())).thenReturn(true);
        when(dateUtils.getDateTime()).thenReturn("2023-10-01");

        // Update stubbing to handle dynamic arguments
        // Update stubbing to handle null or dynamic arguments
        when(processLPARequest.getAccountHistory("0500012345", "t002"))
                .thenReturn(new AccountHistory("0500012345", "t002", "2023-01-01", "2024-01-01", "M", "O", null, null, null, null, null, null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null, null));
        CcpResponse ccpResponse = new CcpResponse();
        Offer offer = new Offer();
        offer.setProgramName("Test Program");
        when(ccpOfferService.getOfferDetails("0500012345", "t002")).thenReturn(ccpResponse);
        when(processLPARequest.extractValidCcpOffer(ccpResponse)).thenReturn(offer);
        when(processLPARequest.isNoNewContractPresent(any())).thenReturn(true);
        // Act
        RedemptionHistory result = lpaAggregatorService.process(processedRedemptionHistory, serviceParameterList);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getDayOfBatchRun());
        assertEquals("Y", result.getCheckInDailyBatchJob());
        assertEquals("N", result.getRedemptionProcessComplete());
    }

    @Test
    void testProcess_EndRedemptionProcess() throws Exception {
        // Arrange
        RedemptionHistory processedRedemptionHistory = new RedemptionHistory();
        processedRedemptionHistory.setReturnDate("03-10-2023");
        processedRedemptionHistory.setDayOfBatchRun(2);
        processedRedemptionHistory.setCheckInDailyBatchJob("N");
        processedRedemptionHistory.setRedemptionProcessComplete("Y");

        // Update stubbing to match the actual arguments
        doReturn("30").when(serviceParamterService).getRecordsByTenantIdAndKey(anyList(), isNull(), isNull());
        when(processLPARequest.isReturnDateValid(any(), any())).thenReturn(false);
        when(dateUtils.getDateTime()).thenReturn("2023-10-01");

        // Act
        RedemptionHistory result = lpaAggregatorService.process(processedRedemptionHistory, serviceParameterList);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getDayOfBatchRun());
        assertEquals("Y", result.getCheckInDailyBatchJob());
        assertEquals("N", result.getRedemptionProcessComplete());
    }


    @Test
    void testCheckForNewContract() {
        // Arrange
        RedemptionHistory redemptionHistory = new RedemptionHistory();
        Date actualReturnDate = new Date();
        int lpaContractSearchWindow = 30;

        AccountHistory accountHistory = new AccountHistory();
        when(processLPARequest.getNewContract(any(), anyString(), anyString()))
                .thenReturn(Collections.singletonList(accountHistory));

        // Act
        List<AccountHistory> result = lpaAggregatorService.checkForNewContract(redemptionHistory, actualReturnDate, lpaContractSearchWindow);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(processLPARequest, times(1)).getNewContract(any(), anyString(), anyString());
    }

    @Test
    void testCallCcp_Success() throws Exception {
        // Arrange
        String accountNumber = "12345";
        RedemptionHistory redemptionHistory = new RedemptionHistory();
        CcpResponse ccpResponse = new CcpResponse();
        Offer offer = new Offer();
        offer.setProgramName("Test Program");
        when(ccpOfferService.getOfferDetails(accountNumber, null)).thenReturn(ccpResponse);
        when(processLPARequest.extractValidCcpOffer(ccpResponse)).thenReturn(offer);

        // Act
        lpaAggregatorService.callCcp(accountNumber, redemptionHistory);

        // Assert
        assertEquals("Test Program", redemptionHistory.getProgramName());
        verify(ccpOfferService, times(1)).getOfferDetails(accountNumber, null);
        verify(processLPARequest, times(1)).extractValidCcpOffer(ccpResponse);
    }

    @Test
    void testCallCcp_NoOffer() throws Exception {
        // Arrange
        String accountNumber = "12345";
        RedemptionHistory redemptionHistory = new RedemptionHistory();
        CcpResponse ccpResponse = new CcpResponse();
        when(ccpOfferService.getOfferDetails(accountNumber, null)).thenReturn(ccpResponse);
        when(processLPARequest.extractValidCcpOffer(ccpResponse)).thenReturn(null);

        // Act
        lpaAggregatorService.callCcp(accountNumber, redemptionHistory);

        // Assert
        assertEquals("N", redemptionHistory.getLpaOfferPresent());
        verify(ccpOfferService, times(1)).getOfferDetails(accountNumber, null);
        verify(processLPARequest, times(1)).extractValidCcpOffer(ccpResponse);
    }

    @Test
    void testCallSparc() throws Exception {
        // Arrange
        AccountHistory accountHistory = new AccountHistory();
        AccountHistory newContract = new AccountHistory();
        RedemptionHistory redemptionHistory = new RedemptionHistory();
        SparcResponse sparcResponse = new SparcResponse();
        when(sparcLpaEligibilityService.getLpaEligibility(accountHistory, newContract, redemptionHistory, null))
                .thenReturn(sparcResponse);

        // Act
        SparcResponse result = lpaAggregatorService.callSparc(accountHistory, newContract, redemptionHistory);

        // Assert
        assertNotNull(result);
        verify(sparcLpaEligibilityService, times(1))
                .getLpaEligibility(accountHistory, newContract, redemptionHistory, null);
    }

    @Test
    void testCallDefi() throws Exception {
        // Arrange
        RedemptionHistory redemptionHistory = new RedemptionHistory();
        //when(processLPARequest.saveNewAccountDetails(any())).thenReturn(null);

        // Act
        lpaAggregatorService.callDefi(redemptionHistory);

        // Assert
        verify(processLPARequest, times(0)).saveNewAccountDetails(any());
    }
}