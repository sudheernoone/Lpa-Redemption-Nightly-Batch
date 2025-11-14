package com.toyota.tfs.LpaRedemptionBatch.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toyota.tfs.LpaRedemptionBatch.model.dao.NewAccountHistory;
import com.toyota.tfs.LpaRedemptionBatch.repository.NewAccountRepository;

@Service
public class NewAccountHistoryDAO {

	@Autowired
	private NewAccountRepository newAccountRepository;
	
	public NewAccountHistory getNewAccountByGroundedAccountNumerAndNewAccountNumber(String groundedAccountNumber,String newAccountNumber,String tenantId) {

		return newAccountRepository.getNewAccountByGroundedAccountNumerAndNewAccountNumber(groundedAccountNumber,newAccountNumber,tenantId);

	}
	
	public int getNewIneligibleAccountCount(String groundedAccountNumber,String tenantId) {

		return newAccountRepository.getNewIneligibleAccountCount(groundedAccountNumber,tenantId);

	}
}
