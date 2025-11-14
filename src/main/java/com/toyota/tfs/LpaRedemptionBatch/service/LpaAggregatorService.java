package com.toyota.tfs.LpaRedemptionBatch.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.toyota.tfs.LpaRedemptionBatch.model.ccp.CcpResponse;
import com.toyota.tfs.LpaRedemptionBatch.model.ccp.Offer;
import com.toyota.tfs.LpaRedemptionBatch.model.dao.AccountHistory;
import com.toyota.tfs.LpaRedemptionBatch.model.dao.NewAccountHistory;
import com.toyota.tfs.LpaRedemptionBatch.model.dao.RedemptionHistory;
import com.toyota.tfs.LpaRedemptionBatch.model.defi.DefiResponse;
import com.toyota.tfs.LpaRedemptionBatch.model.parameter.ServiceParameter;
import com.toyota.tfs.LpaRedemptionBatch.model.sparc.SparcResponse;
import com.toyota.tfs.LpaRedemptionBatch.util.DateUtils;
import com.toyota.tfs.LpaRedemptionBatch.util.ErrorMessageHandler;
import com.toyota.tfs.LpaRedemptionBatch.util.ErrorMessageHandler.MESSAGE_CODE;
import com.toyota.tfs.LpaRedemptionBatch.util.ErrorMessageHandler.REDEMPTION_REJECTION_REASON;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LpaAggregatorService {

    @Value("${db.serviceparameter.lpacontractsearchwindow}")
    String lpaContractSearchWindowProp;
    @Value("${db.serviceparameter.lpaprocesswindow}")
    String lpaProcessWindowProp;

    @Autowired
    CcpOfferService ccpOfferService;
    @Autowired
    DefiSaveLPAService defiSaveLPAService;
    @Autowired
    SparcLpaEligibilityService sparcLpaEligibilityService;
    @Autowired
    ErrorMessageHandler errorMessageHandler;
    @Autowired
    MongoService mongoService;
    @Autowired
    RedemptionErrorService redemptionErrorService;
    @Autowired
    DateUtils dateUtils;
    @Autowired
    ProcessLPARequest processLPARequest;
    @Autowired
    ServiceParamterService serviceParamterService;


    String groundedAccountNumber;
    String tenantId;
    String errorCode;
    String errorMsg;
    boolean endRedemptionProcess;
    boolean ccpCallSuccessful;
    boolean sparcCallSuccessful;
    boolean defiCallSuccessful;
    boolean invalidDealerPurchaseOrPassFlag;
    boolean dealerPurchaseOrPassPresent;


    public RedemptionHistory process(RedemptionHistory redemptionHistoryStartOfBatch, List<ServiceParameter> serviceParameterList) throws Exception {
        // Add the record to the MongoDB
        mongoService.addTransactionToMongo(redemptionHistoryStartOfBatch);

        RedemptionHistory redemptionHistoryEndOfBatch = processGroundingRequest(redemptionHistoryStartOfBatch, serviceParameterList);
        int DayOfBatchRun = redemptionHistoryEndOfBatch.getDayOfBatchRun() + 1;
        redemptionHistoryEndOfBatch.setDayOfBatchRun(DayOfBatchRun);
        redemptionHistoryEndOfBatch.setRecordUpdateDate(dateUtils.getDateTime());
        if (endRedemptionProcess) {
            redemptionHistoryEndOfBatch.setCheckInDailyBatchJob("N");
            redemptionHistoryEndOfBatch.setRedemptionProcessComplete("Y");
        } else {
            redemptionHistoryEndOfBatch.setCheckInDailyBatchJob("Y");
            redemptionHistoryEndOfBatch.setRedemptionProcessComplete("N");
        }
        return redemptionHistoryEndOfBatch;
    }

    public RedemptionHistory processGroundingRequest(RedemptionHistory redemptionHistory, List<ServiceParameter> serviceParameterList) throws Exception {

        errorCode = "";
        errorMsg = "";

        endRedemptionProcess = false;

        ccpCallSuccessful = false;
        sparcCallSuccessful = false;
        defiCallSuccessful = false;

        invalidDealerPurchaseOrPassFlag = false;
        dealerPurchaseOrPassPresent = false;

        // Declare local variables
        boolean dealerPurchasePassFlag;
        boolean dealerPurchasePayoffOrResidual = false;
        AccountHistory accountHistory;
        List<AccountHistory> listOfNewContract;

        groundedAccountNumber = redemptionHistory.getAccountNumber();
        tenantId = redemptionHistory.getTenantId();

        int lpaContractSearchWindow = Integer.parseInt(serviceParamterService.getRecordsByTenantIdAndKey(serviceParameterList, tenantId, lpaContractSearchWindowProp));
        int lpaProcessWindow = Integer.parseInt(serviceParamterService.getRecordsByTenantIdAndKey(serviceParameterList, tenantId, lpaProcessWindowProp));
        log.info(groundedAccountNumber + " lpaContractSearchWindow : " + lpaContractSearchWindow + " lpaProcessWindow : " + lpaProcessWindow);

        String returnDate = redemptionHistory.getReturnDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date actualReturnDate = dateFormat.parse(returnDate);
        Date maxBatchDate = dateFormat.parse(dateFormat.format(DateUtils.addDays(actualReturnDate, lpaProcessWindow)));
        Date currentDate = dateFormat.parse(dateFormat.format(new Date()));
        boolean validReturnDate = processLPARequest.isReturnDateValid(maxBatchDate, currentDate);


        //Account Retrieval
        accountHistory = processLPARequest.getAccountHistory(groundedAccountNumber, tenantId);


        //Data Validation
        //If Account History not available
        if (accountHistory == null) {
            errorCode = ErrorMessageHandler.MESSAGE_CODE.ACCOUNT_NOT_FOUND;
            errorMsg = errorMessageHandler.getErrorMessage(MESSAGE_CODE.ACCOUNT_NOT_FOUND);
            redemptionErrorService.addErrorToTable(groundedAccountNumber, tenantId, errorCode, errorMsg);
            log.info(groundedAccountNumber + "Account: " + groundedAccountNumber + " not found in Account History Table");
            redemptionHistory.setStatus(errorMsg);
            return redemptionHistory;
        }
        //If Account History available but primary SSN not found
        else {
            if (redemptionHistory.getPrimaryBorrowerSsntin() == null || redemptionHistory.getPrimaryBorrowerSsntin().equals("")) {
                if (accountHistory.getPrimaryBorrowerSsntin() == null
                        || accountHistory.getPrimaryBorrowerSsntin().equals("")) {
                    errorCode = ErrorMessageHandler.MESSAGE_CODE.PRIMARY_BORROWER_SSN_NOT_FOUND;
                    errorMsg = errorMessageHandler.getErrorMessage(MESSAGE_CODE.PRIMARY_BORROWER_SSN_NOT_FOUND);
                    redemptionErrorService.addErrorToTable(groundedAccountNumber, tenantId, errorCode, errorMsg);
                    log.info(groundedAccountNumber + "Primary Borrower SSN not found for the Account: " + groundedAccountNumber
                            + " in Account History Table");
                    redemptionHistory.setStatus(errorMsg);
                    return redemptionHistory;
                } else {
                    redemptionHistory.setPrimaryBorrowerSsntin(accountHistory.getPrimaryBorrowerSsntin());
                    redemptionHistory.setCoBorrowerSsn(accountHistory.getCoBorrowerSsn());
                }
            }
        }


        // CCP Call
        if (redemptionHistory.getLpaOfferPresent() == null) {
            log.info(groundedAccountNumber + " CCP API call");
            callCcp(groundedAccountNumber, redemptionHistory);

            if (!ccpCallSuccessful) {
                log.info(groundedAccountNumber + " CCP API call failed to execute");
                endRedemptionProcess = false;
                errorCode = MESSAGE_CODE.CCP_API_CALL_FAILED;
                errorMsg = errorMessageHandler.getErrorMessage(MESSAGE_CODE.CCP_API_CALL_FAILED);
                redemptionHistory.setStatus(errorMsg);
                redemptionErrorService.addErrorToTable(groundedAccountNumber, tenantId, errorCode, errorMsg);
                return redemptionHistory;
            }
        }


        //60days check
        //if currentDate > return Date + 60
        if (!validReturnDate) {
            endRedemptionProcess = true;
            log.info(groundedAccountNumber + " Term expired to check for new contracts for the grounded account:" + groundedAccountNumber);

            if (processLPARequest.isLpaWaiverEligible(redemptionHistory)) {
                log.info(groundedAccountNumber + " LPA Waiver eligible");
                redemptionHistory.setEligibleForRedemption("N");
                redemptionHistory.setLpaRedemptionDecisionDate(dateUtils.getDateTime());
                errorMsg = errorMessageHandler.getErrorMessage(MESSAGE_CODE.TERM_EXPIRED);
                redemptionHistory.setStatus(errorMsg);

                //check for dealer purchase type
                if(processLPARequest.checkDealerPurchasePass(redemptionHistory)){
                    dealerPurchaseOrPassPresent = true;
                } else{
                    if (processLPARequest.checkDealerInfoFromAccountHistory(accountHistory)) {
                        checkDealerPurchaseAndTransportationStatus(accountHistory, redemptionHistory);
                        if (invalidDealerPurchaseOrPassFlag) {
                            log.info(groundedAccountNumber + " Invalid Dealer Information received");
                            return redemptionHistory;
                        } else {
                            log.info(groundedAccountNumber + " Valid Dealer Information received");
                            dealerPurchaseOrPassPresent=true;
                        }
                    }
                }

                if (processLPARequest.isAutomatedGrounding(redemptionHistory) && !dealerPurchaseOrPassPresent) {
                    log.info(groundedAccountNumber + " Dealer Purchase Pass Information not present");
                    redemptionHistory.setRedemptionRejectionReason(REDEMPTION_REJECTION_REASON.DEALER_PURCHASE_TYPE_INELIGIBLE);
                } else {
                    if (processLPARequest.isInEligibleModelPresent(redemptionHistory)) {
                        log.info(groundedAccountNumber + " Ineligible Model Present");
                        redemptionHistory.setRedemptionRejectionReason(REDEMPTION_REJECTION_REASON.MODEL_DOES_NOT_QUALIFY);
                    } else {
                        log.info(groundedAccountNumber + " No Ineligible Model Found");
                        redemptionHistory.setRedemptionRejectionReason(REDEMPTION_REJECTION_REASON.CRITERIA_NOT_MET);
                    }
                }

                log.info(groundedAccountNumber + " DEFI API Call");
                callDefi(redemptionHistory);

                if (!defiCallSuccessful) {
                    log.info(groundedAccountNumber + " DEFI API Call failed to execute for term expired");
                    endRedemptionProcess = false;
                    errorCode = MESSAGE_CODE.TERM_EXPIRED + " - " + MESSAGE_CODE.DEFI_API_CALL_FAILED;
                    errorMsg = errorMessageHandler.getErrorMessage(MESSAGE_CODE.TERM_EXPIRED) + " - "
                            + errorMessageHandler.getErrorMessage(MESSAGE_CODE.DEFI_API_CALL_FAILED);
                    redemptionHistory.setStatus(errorMsg);
                    redemptionHistory.setLpaRedemptionDecisionDate(null);
                    redemptionHistory.setEligibleForRedemption(null);
                    redemptionHistory.setRedemptionRejectionReason(null);
                    redemptionErrorService.addErrorToTable(groundedAccountNumber, tenantId, errorCode, errorMsg);
                }
                return redemptionHistory;
            } else {
                log.info(groundedAccountNumber + " LPA Waiver not eligible");
                errorCode = MESSAGE_CODE.TERM_EXPIRED + " - " + MESSAGE_CODE.LPA_WAIVER_INELIGIBLE;
                errorMsg = errorMessageHandler.getErrorMessage(MESSAGE_CODE.TERM_EXPIRED) + " - "
                        + errorMessageHandler.getErrorMessage(MESSAGE_CODE.LPA_WAIVER_INELIGIBLE);
                redemptionHistory.setStatus(errorMsg);
                return redemptionHistory;
            }
        }

        //New Contract Check
        log.info(groundedAccountNumber + " New Contract Check");
        listOfNewContract = checkForNewContract(redemptionHistory, actualReturnDate, lpaContractSearchWindow);
        List<AccountHistory> listWithoutDuplicates = listOfNewContract.stream().distinct()
                .collect(Collectors.toList());
        // if no new contract found
        if (processLPARequest.isNoNewContractPresent(listWithoutDuplicates)) {
            log.info(groundedAccountNumber + " No new contract found");
            redemptionHistory.setStatus(errorMessageHandler.getErrorMessage(MESSAGE_CODE.NO_NEW_CONTRACT));
            return redemptionHistory;
        }

        log.info(groundedAccountNumber + " New Contract found");
        updateNewAccountHistory(redemptionHistory, listWithoutDuplicates);


        //Send DispoFee Waiver Reason
        if (redemptionHistory.getDispofeeWaiverReasonSent() == null) {
            //Check for dealer purchase type P & R
            if (processLPARequest.checkDealerPurchasePass(redemptionHistory)) {
                if (redemptionHistory.getDealerPurchaseType() != null
                        && (redemptionHistory.getDealerPurchaseType().equalsIgnoreCase("P")
                        || redemptionHistory.getDealerPurchaseType().equalsIgnoreCase("R"))) {
                    dealerPurchasePayoffOrResidual = true;
                }
            } else {
                if (processLPARequest.checkDealerInfoFromAccountHistory(accountHistory)) {
                    if (accountHistory.getDealerPurchaseType() != null
                            && (accountHistory.getDealerPurchaseType().equalsIgnoreCase("P")
                            || accountHistory.getDealerPurchaseType().equalsIgnoreCase("R"))) {
                        dealerPurchasePayoffOrResidual = true;
                    }
                }
            }

            if (dealerPurchasePayoffOrResidual) {
                log.info("Dispo Fee Waiver Reason not sent to defi as dealer purchase type is either P or R");
                redemptionHistory.setDealerPurchaseType(accountHistory.getDealerPurchaseType());
                redemptionHistory.setDealerPurchaseDate(accountHistory.getDealerPurchaseDate());
                redemptionHistory.setDealerPurchasePassComplete("Y");
                redemptionHistory.setDispositionFeeWaiverReason(null);
                redemptionHistory.setDispofeeWaiverReasonSent(null);
                redemptionHistory.setNewDispoAccountNumber(null);
                redemptionHistory.setDispoSentDate(null);
            } else {
                log.info(groundedAccountNumber + " Sending Dispo Fee Waiver Reason");
                sendDispoFeeWaiverReason(redemptionHistory, listWithoutDuplicates.get(0));
                if (!defiCallSuccessful)
                    return redemptionHistory;
            }

        } else {
            log.info(groundedAccountNumber + " Dispo Fee Waiver Reason already sent ");
        }


        //If LPA Waiver not eligible(LPA Offer not present) then end process
        if (!processLPARequest.isLpaWaiverEligible(redemptionHistory)) {
            log.info(groundedAccountNumber + " LPA Waiver not eligible");
            endRedemptionProcess = true;
            errorMsg = errorMessageHandler.getErrorMessage(MESSAGE_CODE.LPA_WAIVER_INELIGIBLE);
            redemptionHistory.setStatus(errorMsg);
            return redemptionHistory;
        }
        log.info(groundedAccountNumber + " LPA Waiver eligible");


        //If Lpa Offer not eligible then Call Defi and end process
        if (!processLPARequest.isReturnDateWithinLpaOffer(redemptionHistory)) {

            endRedemptionProcess = true;
            log.info(groundedAccountNumber + " Return date does not fall within CCP offer start and end date");
            redemptionHistory.setEligibleForRedemption("N");
            redemptionHistory.setRedemptionRejectionReason(REDEMPTION_REJECTION_REASON.CRITERIA_NOT_MET);
            redemptionHistory.setLpaRedemptionDecisionDate(dateUtils.getDateTime());
            errorMsg = errorMessageHandler.getErrorMessage(MESSAGE_CODE.NO_VALID_CCP_OFFER);
            redemptionHistory.setStatus(errorMsg);

            log.info(groundedAccountNumber + " DEFI API Call");
            callDefi(redemptionHistory);

            if (!defiCallSuccessful) {
                log.info(groundedAccountNumber + " DEFI API Call failed to execute for lpa offer ineligible");
                endRedemptionProcess = false;
                errorCode = MESSAGE_CODE.NO_VALID_CCP_OFFER + " - " + MESSAGE_CODE.DEFI_API_CALL_FAILED;
                errorMsg = errorMessageHandler.getErrorMessage(MESSAGE_CODE.NO_VALID_CCP_OFFER) + " - "
                        + errorMessageHandler.getErrorMessage(MESSAGE_CODE.DEFI_API_CALL_FAILED);
                redemptionHistory.setStatus(errorMsg);
                redemptionHistory.setLpaRedemptionDecisionDate(null);
                redemptionHistory.setEligibleForRedemption(null);
                redemptionHistory.setRedemptionRejectionReason(null);
                redemptionErrorService.addErrorToTable(groundedAccountNumber, tenantId, errorCode, errorMsg);
            }
            return redemptionHistory;
        }

        log.info(groundedAccountNumber + " Return date falls within CCP Offer start and end date : LPA Offer Eligible");


        //Manual or Automated Grounding - Dealer Check
        if (processLPARequest.isAutomatedGrounding(redemptionHistory)) {
            log.info(groundedAccountNumber + " Automated grounding");

            //check dealer purchase or pass information from Redemption Table
            dealerPurchasePassFlag = processLPARequest.checkDealerPurchasePass(redemptionHistory);

            if (dealerPurchasePassFlag) {
                log.info(groundedAccountNumber + " Dealer Information received");
                processWithDealerInformation(accountHistory, redemptionHistory);
                if (endRedemptionProcess || !defiCallSuccessful)
                    return redemptionHistory;
            } else {
                log.info(groundedAccountNumber + " Dealer Information not received");
                // check account history for dealer purchase or pass information
                log.info(groundedAccountNumber + " Checking dealer information from account history");
                boolean dealerInfoFromAccountHistory = processLPARequest
                        .checkDealerInfoFromAccountHistory(accountHistory);

                if (dealerInfoFromAccountHistory) {
                    log.info(groundedAccountNumber + " Dealer information found in account history");
                    log.info(groundedAccountNumber + " Validating dealer information");
                    checkDealerPurchaseAndTransportationStatus(accountHistory, redemptionHistory);
                    if (invalidDealerPurchaseOrPassFlag) {
                        log.info(groundedAccountNumber + " Invalid Dealer Information received");
                        return redemptionHistory;
                    } else {
                        log.info(groundedAccountNumber + " Valid Dealer Information received");
                        processWithDealerInformation(accountHistory, redemptionHistory);
                        if (endRedemptionProcess || !defiCallSuccessful)
                            return redemptionHistory;
                    }
                }
                // dealer purchase or pass information not received
                else {
                    endRedemptionProcess = false;
                    errorCode = MESSAGE_CODE.DEALER_DECISION_NOT_FOUND;
                    errorMsg = errorMessageHandler.getErrorMessage(MESSAGE_CODE.DEALER_DECISION_NOT_FOUND);
                    log.info(groundedAccountNumber + " Dealer purchase or pass information not available for the account:"
                            + groundedAccountNumber);
                    redemptionErrorService.addErrorToTable(groundedAccountNumber, tenantId, errorCode, errorMsg);
                    redemptionHistory.setStatus(errorMsg);
                    redemptionHistory.setDealerPurchasePassComplete("N");
                    return redemptionHistory;
                }
            }
        } else {
            log.info(groundedAccountNumber + " Manual grounding");
        }


        //Sparc validation with new contracts
        callSparcForNewContractValidation(redemptionHistory, accountHistory, listWithoutDuplicates, true);


        return redemptionHistory;
    }


    public void sendDispoFeeWaiverReason(RedemptionHistory redemptionHistory, AccountHistory newAccount)
            throws Exception {

        redemptionHistory
                .setDispositionFeeWaiverReason(processLPARequest.getDispositionFeeWaiverReason(newAccount.getVehicleCondition()));


        log.info(groundedAccountNumber + " DEFI API Call");
        callDefi(redemptionHistory);

        redemptionHistory.setDispofeeWaiverReasonSent("Y");
        redemptionHistory.setNewDispoAccountNumber(newAccount.getAccountNumber());
        redemptionHistory.setDispoSentDate(dateUtils.getDateTime());

        if (!defiCallSuccessful) {
            log.info(groundedAccountNumber + " DEFI API Call failed to execute for dispo fee waiver reason");
            endRedemptionProcess = false;
            errorCode = MESSAGE_CODE.DISPOFEE_WAIVER_REASON + " - " + MESSAGE_CODE.DEFI_API_CALL_FAILED;
            errorMsg = errorMessageHandler.getErrorMessage(MESSAGE_CODE.DISPOFEE_WAIVER_REASON) + " - "
                    + errorMessageHandler.getErrorMessage(MESSAGE_CODE.DEFI_API_CALL_FAILED);
            redemptionHistory.setStatus(errorMsg);
            redemptionHistory.setDispositionFeeWaiverReason(null);
            redemptionHistory.setDispofeeWaiverReasonSent(null);
            redemptionHistory.setNewDispoAccountNumber(null);
            redemptionHistory.setDispoSentDate(null);
            redemptionErrorService.addErrorToTable(groundedAccountNumber, tenantId, errorCode, errorMsg);
        }

    }

    private void checkDealerPurchaseAndTransportationStatus(AccountHistory accountHistory,
                                                            RedemptionHistory redemptionHistory) {
        // Check Dealer Purchase Type
        String dealerPurchaseType = accountHistory.getDealerPurchaseType();
        if (dealerPurchaseType != null && !dealerPurchaseType.equals("")) {
            boolean validDealerPurchase = processLPARequest.checkPurchaseFlag(dealerPurchaseType);
            if (!validDealerPurchase) {
                endRedemptionProcess = false;
                log.info(groundedAccountNumber + " Incorrect dealer purchase flag received for the account:" + groundedAccountNumber);
                errorCode = MESSAGE_CODE.DEALER_PURCHASE_TYPE_NOT_VALID;
                errorMsg = errorMessageHandler.getErrorMessage(errorCode);
                redemptionErrorService.addErrorToTable(groundedAccountNumber, tenantId, errorCode, errorMsg);
                redemptionHistory.setDealerPurchaseType(null);
                redemptionHistory.setDealerPurchaseDate(null);
                redemptionHistory.setDealerPurchasePassComplete("N");
                redemptionHistory.setStatus(errorMsg);
                invalidDealerPurchaseOrPassFlag = true;
            } else {
                redemptionHistory.setDealerPurchaseType(dealerPurchaseType);
                redemptionHistory.setDealerPurchaseDate(accountHistory.getDealerPurchaseDate());
                redemptionHistory.setDealerPurchasePassComplete("Y");
            }
        }
        // Check Dealer Transportation Status
        else {
            String transportationStatus = accountHistory.getTransportationStatus();
            if (transportationStatus != null && !transportationStatus.equals("")) {
                boolean validTransportationStatus = processLPARequest.checkTransportationStatus(transportationStatus);
                if (!validTransportationStatus) {
                    log.info(groundedAccountNumber + " Incorrect transportation status received for the account:" + groundedAccountNumber);
                    errorCode = MESSAGE_CODE.TRANSPORTATION_STATUS_NOT_VALID;
                    errorMsg = errorMessageHandler.getErrorMessage(errorCode);
                    endRedemptionProcess = false;
                    redemptionErrorService.addErrorToTable(groundedAccountNumber, tenantId, errorCode, errorMsg);
                    redemptionHistory.setTransportationStatus(null);
                    redemptionHistory.setDealerPurchasePassComplete("N");
                    redemptionHistory.setStatus(errorMsg);
                    invalidDealerPurchaseOrPassFlag = true;
                } else {
                    redemptionHistory.setTransportationStatus(transportationStatus);
                    redemptionHistory.setDealerPurchasePassComplete("Y");
                }
            }
        }
    }

    private void processWithDealerInformation(AccountHistory accountHistory,
                                              RedemptionHistory redemptionHistory) throws Exception {
        //Dealer P,R,X
        if (processLPARequest.isIneligibileDealerPurchaseType(redemptionHistory)) {
            log.info(groundedAccountNumber + " Ineligible Dealer Purchase Type");

            endRedemptionProcess = true;
            redemptionHistory.setEligibleForRedemption("N");
            redemptionHistory.setRedemptionRejectionReason(REDEMPTION_REJECTION_REASON.DEALER_PURCHASE_TYPE_INELIGIBLE);
            redemptionHistory.setLpaRedemptionDecisionDate(dateUtils.getDateTime());
            errorMsg = errorMessageHandler.getErrorMessage(MESSAGE_CODE.DEALER_PURCHASE_TYPE_NOT_ELIGIBLE);
            redemptionHistory.setStatus(errorMsg);

            log.info(groundedAccountNumber + " DEFI API Call");
            callDefi(redemptionHistory);

            if (!defiCallSuccessful) {
                log.info(groundedAccountNumber + " DEFI API Call failed to execute for in eligible dealer");
                endRedemptionProcess = false;
                errorCode = MESSAGE_CODE.DEALER_PURCHASE_TYPE_NOT_ELIGIBLE + " - " + MESSAGE_CODE.DEFI_API_CALL_FAILED;
                errorMsg = errorMessageHandler.getErrorMessage(MESSAGE_CODE.DEALER_PURCHASE_TYPE_NOT_ELIGIBLE) + " - "
                        + errorMessageHandler.getErrorMessage(MESSAGE_CODE.DEFI_API_CALL_FAILED);
                redemptionHistory.setStatus(errorMsg);
                redemptionHistory.setLpaRedemptionDecisionDate(null);
                redemptionHistory.setEligibleForRedemption(null);
                redemptionHistory.setRedemptionRejectionReason(null);
                redemptionErrorService.addErrorToTable(groundedAccountNumber, tenantId, errorCode, errorMsg);
            }
        } else {
            defiCallSuccessful=true; //only for accounts with Purchase Type 'M' or trans type 'O' and defi not called on the same day
            log.info(groundedAccountNumber + " Eligible Dealer Purchase Type or Pass Information");
        }


    }

    // Update New Account History Table
    private void updateNewAccountHistory(RedemptionHistory redemptionHistory,
                                         List<AccountHistory> listWithoutDuplicates) {
        for (AccountHistory newAccount : listWithoutDuplicates) {

            log.info(groundedAccountNumber + " New Contract - Account Number : " + newAccount.getAccountNumber());
            if (processLPARequest.isNewContractDetailsNotPresentInNewAccountHistory(groundedAccountNumber,
                    newAccount.getAccountNumber(), tenantId)) {
                NewAccountHistory newAccountHistory = new NewAccountHistory();
                newAccountHistory.setTenantId(tenantId);
                newAccountHistory.setGroundedAccountNumber(groundedAccountNumber);
                newAccountHistory.setNewAccountNumber(newAccount.getAccountNumber());
                newAccountHistory.setSparcValidationFlag("");
                newAccountHistory.setRedemptionEligibleFlag("");
                newAccountHistory.setRecordInsertDate(dateUtils.getDateTime());

                processLPARequest.saveNewAccount(newAccountHistory);
                log.info(groundedAccountNumber + " New Contract - Account Number : " + newAccount.getAccountNumber()
                        + " inserted in New Account History");
            }
        }

    }

    private void callSparcForNewContractValidation(RedemptionHistory redemptionHistory, AccountHistory groundedAccountHistory,
                                                   List<AccountHistory> newAccountlist, boolean validReturnDate) throws Exception {
        boolean sparcValidation = false;
        defiCallSuccessful=false;
        for (AccountHistory newAccount : newAccountlist) {

            if (!sparcValidation) {
                log.info(groundedAccountNumber + " New Account Number for Sparc Call:" + newAccount.getAccountNumber());
                NewAccountHistory newAccountHistory = processLPARequest
                        .getNewAccountByGroundedAccountNumerAndNewAccountNumber(redemptionHistory.getAccountNumber(),
                                newAccount.getAccountNumber(), redemptionHistory.getTenantId());
                SparcResponse sparcResponse = callSparc(groundedAccountHistory, newAccount, redemptionHistory);
                if (sparcResponse != null) {
                    sparcCallSuccessful = true;
                    if (sparcResponse.getLpaQualifiers() != null && sparcResponse.getLpaQualifiers().size() > 0
                            && sparcResponse.getLpaQualifiers().get(0) != null
                            && sparcResponse.getLpaQualifiers().get(0).getPrograms() != null
                            && sparcResponse.getLpaQualifiers().get(0).getPrograms().size() > 0
                            && sparcResponse.getLpaQualifiers().get(0).getPrograms().get(0) != null) {

                        log.info(groundedAccountNumber + " LPA Eligible - SPARC");
                        endRedemptionProcess = true;

                        redemptionHistory.setEligibleForRedemption("Y");
                        redemptionHistory.setRedemptionRejectionReason(null);
                        redemptionHistory.setMaximumNoOfPaymentsToWaive(sparcResponse.getLpaQualifiers().get(0)
                                .getPrograms().get(0).getWaivableMonthlyPayments());
                        redemptionHistory.setMaximumAmountToWaive(
                                sparcResponse.getLpaQualifiers().get(0).getPrograms().get(0).getValue());
                        redemptionHistory.setBuydownId(
                                sparcResponse.getLpaQualifiers().get(0).getPrograms().get(0).getOptionDetailId());

                        redemptionHistory.setNewAccountNumber(newAccount.getAccountNumber());
                        redemptionHistory
                                .setLpaProgramId(sparcResponse.getLpaQualifiers().get(0).getPrograms().get(0).getId());

                        redemptionHistory.setLpaRedemptionDecisionDate(dateUtils.getDateTime());
                        redemptionHistory.setStatus(MESSAGE_CODE.SUCCESS_STATUS);

                        log.info(groundedAccountNumber + " DEFI API Call");
                        callDefi(redemptionHistory);
                        if (!defiCallSuccessful) {

                            log.info(groundedAccountNumber + " DEFI API Call failed to execute for Success scenario");
                            errorCode = MESSAGE_CODE.DEFI_API_CALL_FAILED;
                            errorMsg = "LPA Eligible - " + errorMessageHandler.getErrorMessage(errorCode);

                            endRedemptionProcess = false;
                            redemptionHistory.setEligibleForRedemption(null);
                            redemptionHistory.setMaximumNoOfPaymentsToWaive(null);
                            redemptionHistory.setMaximumAmountToWaive(null);
                            redemptionHistory.setBuydownId(null);
                            redemptionHistory.setNewAccountNumber(null);
                            redemptionHistory.setLpaProgramId(null);
                            redemptionHistory.setLpaRedemptionDecisionDate(null);
                            redemptionHistory.setStatus(errorMsg);
                            redemptionErrorService.addErrorToTable(groundedAccountNumber, tenantId, errorCode, errorMsg);
                            break;
                        }

                        newAccountHistory.setSparcValidationFlag("Y");
                        newAccountHistory.setRedemptionEligibleFlag("Y");
                        newAccountHistory.setRecordUpdateDate(dateUtils.getDateTime());

                        sparcValidation = true;
                    }
                    // if sparc not eligible
                    else {
                        log.info(groundedAccountNumber + "LPA not Eligible - SPARC");

                        redemptionHistory.setStatus(errorMessageHandler.getErrorMessage(MESSAGE_CODE.LPA_NOT_ELIGIBLE));
                        newAccountHistory.setSparcValidationFlag("Y");
                        newAccountHistory.setRedemptionEligibleFlag("N");
                        newAccountHistory.setRecordUpdateDate(dateUtils.getDateTime());
                    }

                    processLPARequest.saveNewAccountDetails(newAccountHistory);
                }
                // Sparc API call failed - sparcResponse is null
                else {
                    sparcCallSuccessful = false;
                    log.info(groundedAccountNumber + "Sparc api call failed to execute");
                    errorCode = MESSAGE_CODE.SPARC_API_CALL_FAILED;
                    errorMsg = "New Account:" + newAccount.getAccountNumber() + "-"
                            + errorMessageHandler.getErrorMessage(MESSAGE_CODE.SPARC_API_CALL_FAILED);
                    redemptionHistory.setStatus(errorMsg);
                    redemptionErrorService.addErrorToTable(groundedAccountNumber, tenantId, errorCode, errorMsg);
                    break;
                }

            }

        }

    }


    public List<AccountHistory> checkForNewContract(RedemptionHistory redemptionHistory, Date actualReturnDate, int lpaContractSearchWindow) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String maxContractDate = dateFormat.format(DateUtils.addDays(actualReturnDate, lpaContractSearchWindow));
        String minContractDate = dateFormat.format(DateUtils.removeDays(actualReturnDate, lpaContractSearchWindow));
        return processLPARequest.getNewContract(redemptionHistory, maxContractDate, minContractDate);
    }

    public void callCcp(String accountNumber, RedemptionHistory redemptionHistory) throws Exception {
        CcpResponse ccpResponse = ccpOfferService.getOfferDetails(accountNumber,tenantId);
        if (ccpResponse != null) {
            ccpCallSuccessful = true;
            Offer offer = processLPARequest.extractValidCcpOffer(ccpResponse);
            if (offer != null) {
                redemptionHistory.setProgramName(offer.getProgramName());
                redemptionHistory.setOfferName(offer.getOfferName());
                redemptionHistory.setOfferStartDate(offer.getOfferStartDate());
                redemptionHistory.setOfferEndDate(offer.getOfferEndDate());
                redemptionHistory.setLpaOfferPresent("Y");
                redemptionHistory.setLpaOfferEligible(processLPARequest.checkCcpOfferEligibilty(offer, redemptionHistory.getReturnDate()));
            } else {
                redemptionHistory.setLpaOfferPresent("N");
            }
        } else {
            ccpCallSuccessful = false;
        }
    }

    public SparcResponse callSparc(AccountHistory accountHistory, AccountHistory newContract,
                                   RedemptionHistory redemptionHistory) throws Exception {
        return sparcLpaEligibilityService.getLpaEligibility(accountHistory, newContract, redemptionHistory,tenantId);
    }

    public void callDefi(RedemptionHistory redemptionHistory) throws Exception {
        DefiResponse defiResponse = null;
        defiResponse = defiSaveLPAService.processTransaction(redemptionHistory,tenantId);

        defiCallSuccessful = defiResponse != null;

    }

}
