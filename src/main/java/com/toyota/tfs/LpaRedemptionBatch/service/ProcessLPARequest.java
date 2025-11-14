package com.toyota.tfs.LpaRedemptionBatch.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProcessLPARequest {

	@Value("${business.grounding.automated.userid}")
	public String automatedUserId;
	@Autowired
	public AccountHistoryDAO accountHistoryDAO;
	@Autowired
	public NewAccountHistoryDAO newAccountHistoryDAO;
	@Autowired
	public RedemptionHistoryRepository redemptionHistoryRepository;
	@Autowired
	public NewAccountRepository newAccountRepository;

	public void saveGroundingRequest(RedemptionHistory redemptionHistory) {
		redemptionHistoryRepository.save(redemptionHistory);
	}

	public void saveNewAccount(NewAccountHistory newAccount) {
		newAccountRepository.save(newAccount);
	}
	
	public void saveNewAccountDetails(NewAccountHistory newAccount) {
		newAccountRepository.save(newAccount);
	}

	public boolean isAutomatedGrounding(RedemptionHistory redemptionHistory) {
		return (redemptionHistory.getUserId() != null
				&& redemptionHistory.getUserId().equalsIgnoreCase(automatedUserId));
	}

	public AccountHistory getAccountHistory(String accountNumber,String tenantId) {
		return accountHistoryDAO.getAccount(accountNumber,tenantId);
	}

	public boolean checkDealerPurchasePass(RedemptionHistory redemptionHistory) {
		return (redemptionHistory != null
				&& redemptionHistory.getDealerPurchasePassComplete() != null
				&& redemptionHistory.getDealerPurchasePassComplete().equalsIgnoreCase("Y"));
	}

	public boolean checkDealerInfoFromAccountHistory(AccountHistory accountHistory) {
		return (accountHistory != null && ((accountHistory.getDealerPurchaseType() != null
				&& !accountHistory.getDealerPurchaseType().equals(""))
				|| (accountHistory.getTransportationStatus() != null
				&& !accountHistory.getTransportationStatus().equals(""))));
	}

	public boolean checkPurchaseFlag(String dealerPurchaseType) {
		return (dealerPurchaseType != null &&
				(dealerPurchaseType.equalsIgnoreCase("M")
						|| dealerPurchaseType.equalsIgnoreCase("P")
						|| dealerPurchaseType.equalsIgnoreCase("R")
						|| dealerPurchaseType.equalsIgnoreCase("X")));
	}

	public boolean checkTransportationStatus(String transportationStatus) {
		return (transportationStatus != null && transportationStatus.equalsIgnoreCase("O"));
	}


	public List<AccountHistory> getNewContract(RedemptionHistory redemptionHistory, String maxDate, String minDate) {

		List<AccountHistory> listOfNewContract = new ArrayList<AccountHistory>();

		listOfNewContract.addAll(accountHistoryDAO.fetchNewContractForPrimaryBorrowerAsBorrower(
				redemptionHistory.getPrimaryBorrowerSsntin(), maxDate, minDate, redemptionHistory.getAccountNumber(), redemptionHistory.getTenantId()));
		listOfNewContract.addAll(accountHistoryDAO.fetchNewContractForPrimaryBorrowerAsCoBorrower(
				redemptionHistory.getPrimaryBorrowerSsntin(), maxDate, minDate, redemptionHistory.getAccountNumber(), redemptionHistory.getTenantId()));

		if (redemptionHistory.getCoBorrowerSsn()!=null && !redemptionHistory.getCoBorrowerSsn().equals("")) {
			listOfNewContract.addAll(accountHistoryDAO.fetchNewContractForCoBorrowerAsBorrower(
					redemptionHistory.getCoBorrowerSsn(), maxDate, minDate, redemptionHistory.getAccountNumber(), redemptionHistory.getTenantId()));
			listOfNewContract.addAll(accountHistoryDAO.fetchNewContractForCoBorrowerAsCoBorrower(
					redemptionHistory.getCoBorrowerSsn(), maxDate, minDate, redemptionHistory.getAccountNumber(), redemptionHistory.getTenantId()));
		}

		return listOfNewContract;

	}

	public boolean isNoNewContractPresent(List<AccountHistory> listOfNewContract) {
		return listOfNewContract != null && listOfNewContract.size() == 0;
	}

	

	public Offer extractValidCcpOffer(CcpResponse ccpResponse) {
		Offer offer;
		if (ccpResponse != null && ccpResponse.getCcpResponseItems() != null
				&& ccpResponse.getCcpResponseItems().size() > 0) {
			for (CcpResponseItems CcpResponseItem : ccpResponse.getCcpResponseItems()) {
				offer = new Offer();
				for (CcpResponseValues ccpResponseValue : CcpResponseItem.getCcpResponseValues()) {
					if (ccpResponseValue.getName() != null
							&& ccpResponseValue.getName().equalsIgnoreCase("OFFER_START_DATE"))
						offer.setOfferStartDate(ccpResponseValue.getValue());
					else if (ccpResponseValue.getName() != null
							&& ccpResponseValue.getName().equalsIgnoreCase("OFFER_END_DATE"))
						offer.setOfferEndDate(ccpResponseValue.getValue());
					else if (ccpResponseValue.getName() != null
							&& ccpResponseValue.getName().equalsIgnoreCase("OFFER_NAME"))
						offer.setOfferName(ccpResponseValue.getValue());
					else if (ccpResponseValue.getName() != null
							&& ccpResponseValue.getName().equalsIgnoreCase("PROGRAM_NAME"))
						offer.setProgramName(ccpResponseValue.getValue());
				}
				return offer;
			}
		}
		return null;
	}
	
	public String checkCcpOfferEligibilty(Offer offer,String returnDateFromRH) throws ParseException {
		SimpleDateFormat sdfAPI = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat sdfDB = new SimpleDateFormat("yyyy-MM-dd");
		Date offerStartDate = sdfAPI.parse(offer.getOfferStartDate());
		Date offerEndDate = sdfAPI.parse(offer.getOfferEndDate());
		Date returnDate = sdfAPI.parse(sdfAPI.format(sdfDB.parse(returnDateFromRH)));

		if (offerStartDate.compareTo(returnDate) <= 0 && returnDate.compareTo(offerEndDate) <= 0)
			return "Y";
		else
			return "N";
	}
	
	public boolean isReturnDateValid(Date maxBatchDate, Date currentDate) {
        return (currentDate.compareTo(maxBatchDate)<0);
    }
	
	public boolean isLpaWaiverEligible(RedemptionHistory redemptionHistory) {
		return (redemptionHistory!=null && redemptionHistory.getLpaOfferPresent()!=null && redemptionHistory.getLpaOfferPresent().equalsIgnoreCase("Y"));
	}
	
	public boolean isReturnDateWithinLpaOffer(RedemptionHistory redemptionHistory) {
		return (redemptionHistory!=null && redemptionHistory.getLpaOfferEligible()!=null && redemptionHistory.getLpaOfferEligible().equalsIgnoreCase("Y"));
	}
	
	public boolean isInEligibleModelPresent(RedemptionHistory redemptionHistory) {
		return (newAccountHistoryDAO.getNewIneligibleAccountCount(redemptionHistory.getAccountNumber(),redemptionHistory.getTenantId())>0);
	}

	
	
	
	
	public boolean isIneligibileDealerPurchaseType(RedemptionHistory redemptionHistory) {
		return (redemptionHistory != null && redemptionHistory.getDealerPurchaseType() != null
				&& (redemptionHistory.getDealerPurchaseType().equalsIgnoreCase("P")
						|| redemptionHistory.getDealerPurchaseType().equalsIgnoreCase("R")
						|| redemptionHistory.getDealerPurchaseType().equalsIgnoreCase("X")));

	}

	public boolean isEligibleDealerPurchaseType(AccountHistory accountHistory) {
		return (accountHistory != null
				&& ((accountHistory.getDealerPurchaseType() != null
				     && accountHistory.getDealerPurchaseType().equalsIgnoreCase("M"))
						|| (accountHistory.getTransportationStatus() != null
				            && accountHistory.getTransportationStatus().equalsIgnoreCase("O"))));

	}

	public String getDispositionFeeWaiverReason(String vehicleCondition) {
		if (vehicleCondition != null
				&& (vehicleCondition.equalsIgnoreCase("N")
				|| vehicleCondition.equalsIgnoreCase("D")))
			return "New Orig - New Vehicle";
		else
			return "New Orig - CPO Vehicle";

	}
	
	public boolean isNewContractDetailsNotPresentInNewAccountHistory(String groundedAccountNumber,String newAccountNumber,String tenantId) {
		return (newAccountHistoryDAO.getNewAccountByGroundedAccountNumerAndNewAccountNumber(groundedAccountNumber,newAccountNumber,tenantId) == null);
	}
	public NewAccountHistory getNewAccountByGroundedAccountNumerAndNewAccountNumber(String groundedAccountNumber,String newAccountNumber,String tenantId) {
		return newAccountHistoryDAO.getNewAccountByGroundedAccountNumerAndNewAccountNumber(groundedAccountNumber,newAccountNumber,tenantId);
	}

	public void deleteNewAccountHistory(NewAccountHistory accountList) {
		newAccountRepository.delete(accountList);
	}
}
