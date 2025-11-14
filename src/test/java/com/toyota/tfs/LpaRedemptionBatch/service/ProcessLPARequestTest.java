package com.toyota.tfs.LpaRedemptionBatch.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.toyota.tfs.LpaRedemptionBatch.dao.AccountHistoryDAO;
import com.toyota.tfs.LpaRedemptionBatch.dao.NewAccountHistoryDAO;
import com.toyota.tfs.LpaRedemptionBatch.model.ccp.CcpResponse;
import com.toyota.tfs.LpaRedemptionBatch.model.ccp.CcpResponseItems;
import com.toyota.tfs.LpaRedemptionBatch.model.ccp.CcpResponseValues;
import com.toyota.tfs.LpaRedemptionBatch.model.ccp.Offer;
import com.toyota.tfs.LpaRedemptionBatch.model.dao.AccountHistory;
import com.toyota.tfs.LpaRedemptionBatch.model.dao.NewAccountHistory;
import com.toyota.tfs.LpaRedemptionBatch.model.dao.RedemptionHistory;
import com.toyota.tfs.LpaRedemptionBatch.repository.NewAccountRepository;
import com.toyota.tfs.LpaRedemptionBatch.repository.RedemptionHistoryRepository;

@ExtendWith(MockitoExtension.class)
class ProcessLPARequestTest {

    @Mock
    private AccountHistoryDAO accountHistoryDAO;
    @Mock
    private NewAccountHistoryDAO newAccountHistoryDAO;

    @Mock
    private NewAccountRepository newAccountRepository;
    @Mock
    private RedemptionHistoryRepository redemptionHistoryRepository;

    @InjectMocks
    private ProcessLPARequest processLPARequest;

    private RedemptionHistory redemptionHistory;

    private NewAccountHistory newAccountHistory;
    AccountHistory accountHistory;

    @BeforeEach
    void setUp() {
        redemptionHistory = new RedemptionHistory();
        redemptionHistory.setAccountNumber("12345");
        redemptionHistory.setTenantId("tenant1");
        redemptionHistory.setDealerPurchaseType("M");
        redemptionHistory.setLpaOfferPresent("Y");
        redemptionHistory.setLpaOfferEligible("Y");
        redemptionHistory.setUserId("AUTO_USER");

        newAccountHistory = new NewAccountHistory();
        newAccountHistory.setNewAccountNumber("67890");
        newAccountHistory.setGroundedAccountNumber("12345");

        accountHistory = new AccountHistory();
        accountHistory.setDealerPurchaseType("M");

        processLPARequest.automatedUserId="AUTO_USER";

        processLPARequest.accountHistoryDAO = accountHistoryDAO;
        processLPARequest.newAccountHistoryDAO = newAccountHistoryDAO;

        processLPARequest.newAccountRepository = newAccountRepository;
        processLPARequest.redemptionHistoryRepository = redemptionHistoryRepository;
    }

    @Test
    void testSaveAccounts() {
        processLPARequest.saveGroundingRequest(redemptionHistory);
        processLPARequest.saveNewAccount(newAccountHistory);
        processLPARequest.saveNewAccountDetails(newAccountHistory);
    }

    @Test
    void testIsAutomatedGrounding() {
        boolean result = processLPARequest.isAutomatedGrounding(redemptionHistory);
        assertTrue(result);
    }

    @Test
    void testGetAccountHistory() {
        AccountHistory accountHistory = new AccountHistory();
       // when(accountHistoryDAO.getAccount("12345", "tenant1")).thenReturn(accountHistory);

        AccountHistory result = processLPARequest.getAccountHistory("12345", "tenant1");

        //assertNotNull(result);
        //verify(accountHistoryDAO, times(1)).getAccount("12345", "tenant1");
    }

    @Test
    void testCheckDealerPurchasePass() {
        redemptionHistory.setDealerPurchasePassComplete("Y");

        boolean result = processLPARequest.checkDealerPurchasePass(redemptionHistory);

        assertTrue(result);
    }

    @Test
    void testIsLpaWaiverEligible() {
        boolean result = processLPARequest.isLpaWaiverEligible(redemptionHistory);

        assertTrue(result);
    }

    @Test
    void testIsReturnDateValid() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date maxBatchDate = sdf.parse("2023-10-01");
        Date currentDate = sdf.parse("2023-09-30");

        boolean result = processLPARequest.isReturnDateValid(maxBatchDate, currentDate);

        assertTrue(result);
    }

    @Test
    void testIsNoNewContractPresent() {
       // when(accountHistoryDAO.fetchNewContractForPrimaryBorrowerAsBorrower(any(), any(), any(), any(), any()))
        //        .thenReturn(Collections.emptyList());

        List<AccountHistory> newContracts = processLPARequest.getNewContract(redemptionHistory, "2023-10-01", "2023-09-01");

        assertTrue(processLPARequest.isNoNewContractPresent(newContracts));
    }

    @Test
    void testDeleteNewAccountHistory() {
        NewAccountHistory newAccountHistory = new NewAccountHistory();

        processLPARequest.deleteNewAccountHistory(newAccountHistory);

        verify(newAccountRepository, times(1)).delete(newAccountHistory);
    }


    @Test
    void checkDealerInfoFromAccountHistory() {
        processLPARequest.checkDealerInfoFromAccountHistory(accountHistory);
    }

    @Test
    void checkPurchaseFlag() {
        processLPARequest.checkPurchaseFlag("X");
    }

    @Test
    void checkTransportationStatus() {
        processLPARequest.checkTransportationStatus("O");
    }

    @Test
    void testExtractValidCcpOffer() {
        CcpResponse ccpResponse = new CcpResponse();

        CcpResponseItems ccpResponseItem = new CcpResponseItems();

        CcpResponseValues ccpResponseValue1 = new CcpResponseValues();
        ccpResponseValue1.setName("OFFER_START_DATE");
        ccpResponseValue1.setValue("01/01/2023");
        CcpResponseValues ccpResponseValue2 = new CcpResponseValues();
        ccpResponseValue2.setName("OFFER_END_DATE");
        ccpResponseValue2.setValue("01/01/2023");
        CcpResponseValues ccpResponseValue3 = new CcpResponseValues();
        ccpResponseValue3.setName("OFFER_NAME");
        ccpResponseValue3.setValue("Special Offer");
        CcpResponseValues ccpResponseValue4 = new CcpResponseValues();
        ccpResponseValue4.setName("PROGRAM_NAME");
        ccpResponseValue4.setValue("LPA Program");
        List<CcpResponseValues> ccpResponseValueList = new ArrayList<>();
        ccpResponseValueList.add(ccpResponseValue1);
        ccpResponseValueList.add(ccpResponseValue2);
        ccpResponseValueList.add(ccpResponseValue3);
        ccpResponseValueList.add(ccpResponseValue4);

        ccpResponseItem.setCcpResponseValues(ccpResponseValueList);
        List<CcpResponseItems> ccpResponseItemsList = new ArrayList<>();
        ccpResponseItemsList.add(ccpResponseItem);

        ccpResponse.setCcpResponseItems(ccpResponseItemsList);

        assertNotNull(processLPARequest.extractValidCcpOffer(ccpResponse));
    }

    @Test
    void testCheckCcpOfferEligibilty() throws ParseException {
        Offer offer= new Offer();
        offer.setOfferStartDate("01/01/2023");
        offer.setOfferEndDate("12/31/2023");
        String result = processLPARequest.checkCcpOfferEligibilty(offer,"2023-05-05");

        assertEquals("Y",result);
    }

    @Test
    void testIsReturnDateWithinLpaOffer() {
        processLPARequest.isReturnDateWithinLpaOffer(redemptionHistory);
    }

    @Test
    void isInEligibleModelPresent() {
        processLPARequest.isInEligibleModelPresent(redemptionHistory);
    }

    @Test
    void isIneligibileDealerPurchaseType() {
        processLPARequest.isIneligibileDealerPurchaseType(redemptionHistory);
    }

    @Test
    void isEligibleDealerPurchaseType() {

        processLPARequest.isEligibleDealerPurchaseType(accountHistory);
    }

    @Test
    void getDispositionFeeWaiverReason() {
        processLPARequest.getDispositionFeeWaiverReason("N");
    }

    @Test
    void isNewContractDetailsNotPresentInNewAccountHistory() {
        processLPARequest.isNewContractDetailsNotPresentInNewAccountHistory("0500012345", "0500067890", "t002");
    }

    @Test
    void getNewAccountByGroundedAccountNumerAndNewAccountNumber() {
        processLPARequest.getNewAccountByGroundedAccountNumerAndNewAccountNumber("0500012345", "0500067890", "t002");
    }
}