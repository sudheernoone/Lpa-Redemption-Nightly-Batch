package com.toyota.tfs.LpaRedemptionBatch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.toyota.tfs.LpaRedemptionBatch.model.parameter.ServiceParameter;

@Repository
public interface ServiceParameterRepository extends JpaRepository<ServiceParameter,String> {
	
	@Query(value="select * from public.service_parameter where service_name=?1 and active_flag=?2",nativeQuery = true)
    public List<ServiceParameter> findByServiceNameAndActiveFlag(String serviceName,String activeFlag);
}
