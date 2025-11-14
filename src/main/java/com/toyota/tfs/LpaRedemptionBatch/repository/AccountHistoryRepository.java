package com.toyota.tfs.LpaRedemptionBatch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.toyota.tfs.LpaRedemptionBatch.model.dao.AccountHistory;

@Repository
public interface AccountHistoryRepository extends JpaRepository<AccountHistory,Long> {
	
	@Query(value="select * from lpa_account_history where account_number=? and tenant_id=?",nativeQuery = true)
	public AccountHistory getAccountByAccountNumber(String accountNumber,String tenantId);
	
	@Query(value="select * from lpa_account_history where primary_borrower_ssntin=?1 " +
			"and contract_start_date<=?2 and contract_start_date>=?3 and account_number!=?4 and tenant_id=?5"+
			" and account_number not in (select new_account_number from lpa_new_account_history where grounded_account_number=?4 and redemption_eligible_flag!='')",nativeQuery = true)
	public List<AccountHistory> getAccountByPrimaryBorrowerSsnTin(String ssn,String maxDate, String minDate,String accountNumber,String tenantId);
	
	@Query(value="select * from lpa_account_history where co_borrower_ssn=?1 " +
			"and contract_start_date<=?2 and contract_start_date>=?3 and account_number!=?4 and tenant_id=?5"+
			" and account_number not in (select new_account_number from lpa_new_account_history where grounded_account_number=?4 and redemption_eligible_flag!='')",nativeQuery = true)
	public List<AccountHistory> getAccountByCoBorrowerSsn(String ssn,String maxDate, String minDate,String accountNumber,String tenantId);
}
