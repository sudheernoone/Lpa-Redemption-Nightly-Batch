package com.toyota.tfs.LpaRedemptionBatch.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.toyota.tfs.LpaRedemptionBatch.exception.ServiceParameterException;
import com.toyota.tfs.LpaRedemptionBatch.model.parameter.ServiceParameter;
import com.toyota.tfs.LpaRedemptionBatch.repository.ServiceParameterRepository;
import com.toyota.tfs.LpaRedemptionBatch.util.ServiceParameterValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ServiceParamterService {

	@Autowired
	ServiceParameterRepository serviceParameterRepository;
	
	@Autowired
	ServiceParameterValidator serviceParameterValidator;

	public List<ServiceParameter> getRecord() throws ServiceParameterException, Exception {
		List<ServiceParameter> serviceParameterList = serviceParameterRepository.findByServiceNameAndActiveFlag("LeasePullAhead","Y");
		if(serviceParameterValidator.validateServiceParameterList(serviceParameterList))
				return serviceParameterList;
			else
				throw new ServiceParameterException("Service Parameter not found");
	}
	
	public String getRecordsByTenantIdAndKey(List<ServiceParameter> serviceParameterList,String tenantId,String key) {
	
		for(ServiceParameter serviceParameter : serviceParameterList) {
			if(serviceParameter.getTenantId().equalsIgnoreCase(tenantId) && serviceParameter.getKey().equalsIgnoreCase(key))
				return serviceParameter.getValue();
		}
		return null;
	}
	

}
