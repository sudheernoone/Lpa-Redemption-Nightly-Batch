package com.toyota.tfs.LpaRedemptionBatch.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toyota.tfs.LpaRedemptionBatch.model.dao.AccountHistory;
import com.toyota.tfs.LpaRedemptionBatch.repository.AccountHistoryRepository;

@Service
public class AccountHistoryDAO {

	@Autowired
	private AccountHistoryRepository accountHistoryRepository;

	public AccountHistory getAccount(String accountNumber,String tenantId) {

		return accountHistoryRepository.getAccountByAccountNumber(accountNumber,tenantId);

	}

	public List<AccountHistory> fetchNewContractForPrimaryBorrowerAsBorrower(String primaryBorrowerSsnTin,
																			 String maxDate, String minDate,String accountNumber,String tenantId) {

		return accountHistoryRepository.getAccountByPrimaryBorrowerSsnTin(primaryBorrowerSsnTin,maxDate,minDate,accountNumber,tenantId);
	}
	
	public List<AccountHistory> fetchNewContractForPrimaryBorrowerAsCoBorrower(String primaryBorrowerSsnTin,
																			   String maxDate, String minDate,String accountNumber,String tenantId) {

		return accountHistoryRepository.getAccountByCoBorrowerSsn(primaryBorrowerSsnTin,maxDate,minDate,accountNumber,tenantId);
	}
	
	public List<AccountHistory> fetchNewContractForCoBorrowerAsBorrower(String coBorrowerSsn,
																		String maxDate, String minDate,String accountNumber,String tenantId) {

		return accountHistoryRepository.getAccountByPrimaryBorrowerSsnTin(coBorrowerSsn,maxDate,minDate,accountNumber,tenantId);
	}
	
	public List<AccountHistory> fetchNewContractForCoBorrowerAsCoBorrower(String coBorrowerSsn,
																		  String maxDate, String minDate,String accountNumber,String tenantId) {

		return accountHistoryRepository.getAccountByCoBorrowerSsn(coBorrowerSsn,maxDate,minDate,accountNumber,tenantId);
	}

	

}
