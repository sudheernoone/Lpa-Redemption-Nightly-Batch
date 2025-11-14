package com.toyota.tfs.LpaRedemptionBatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.toyota.tfs.LpaRedemptionBatch.model.dao.NewAccountHistory;

@Repository
public interface NewAccountRepository extends JpaRepository<NewAccountHistory,Long> {

    
    @Query(value="select * from lpa_new_account_history where grounded_account_number=?1 and new_account_number=?2 and tenant_id=?3",nativeQuery = true)
    public NewAccountHistory getNewAccountByGroundedAccountNumerAndNewAccountNumber(String groundedAccountNumber,String newAccountNumber,String tenantId);
    
    @Query(value="select count(*) from lpa_new_account_history where grounded_account_number=?1 and tenant_id=?2 and redemption_eligible_flag='N'",nativeQuery = true)
    public int getNewIneligibleAccountCount(String groundedAccountNumber,String tenantId);
}
