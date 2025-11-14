package com.toyota.tfs.LpaRedemptionBatch.process;

import java.util.List;

import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.toyota.tfs.LpaRedemptionBatch.model.dao.RedemptionHistory;
import com.toyota.tfs.LpaRedemptionBatch.model.parameter.ServiceParameter;
import com.toyota.tfs.LpaRedemptionBatch.service.LpaAggregatorService;
import com.toyota.tfs.LpaRedemptionBatch.service.ProcessLPARequest;
import com.toyota.tfs.LpaRedemptionBatch.service.ServiceParamterService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AccountProcessor implements ItemProcessor<RedemptionHistory, RedemptionHistory> {

	@Autowired
	LpaAggregatorService lpaAggregatorService;
	@Autowired
	ProcessLPARequest processLPARequest;
	@Autowired
	ServiceParamterService serviceParamterService;

	List<ServiceParameter> serviceParameterList;

	@BeforeStep
	public void fetchServiceParameters() throws Exception {
		serviceParameterList=null;
		serviceParameterList=serviceParamterService.getRecord();
	}

	@Override
	public RedemptionHistory process(RedemptionHistory redemptionHistory) throws Exception {
		log.info("**** Process for Grounding Account Number: " + redemptionHistory.getAccountNumber() +" - Start ****");
		RedemptionHistory redemptionHistory1=lpaAggregatorService.process(redemptionHistory,serviceParameterList);
		processLPARequest.saveGroundingRequest(redemptionHistory1);
		log.info("**** Process for Grounding Account Number: " + redemptionHistory.getAccountNumber() +" - End ****");
		return redemptionHistory;
	}

}
