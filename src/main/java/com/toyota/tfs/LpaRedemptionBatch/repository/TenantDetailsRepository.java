package com.toyota.tfs.LpaRedemptionBatch.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.toyota.tfs.LpaRedemptionBatch.model.dao.TenantDetails;

@Repository
public interface TenantDetailsRepository extends JpaRepository<TenantDetails,String> {
	
	 @Query(value="select * from public.tenant_details where tenant_id=?1",nativeQuery = true)
	 public TenantDetails getTenantDetails(String tenant_id);
}
