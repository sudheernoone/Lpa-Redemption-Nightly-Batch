package com.toyota.tfs.LpaRedemptionBatch.service;


import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.toyota.tfs.LpaRedemptionBatch.model.dao.RedemptionError;
import com.toyota.tfs.LpaRedemptionBatch.repository.RedemptionErrorRepository;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class RedemptionErrorService {

    @Autowired
    RedemptionErrorRepository redemptionErrorRepository;

    public void addErrorToTable(String accountNumber,String tenantId, String errorCode, String errorMsg){
        RedemptionError redemptionError = new RedemptionError();

        redemptionError.setAccountNumber(accountNumber);
        redemptionError.setTenantId(tenantId);
        redemptionError.setErrorCode(errorCode);
        redemptionError.setErrorMessage(errorMsg);
        redemptionError.setRecordInsertDate(LocalDateTime.now().toString());
        redemptionError.setMailSent("N");

        redemptionErrorRepository.save(redemptionError);

    }

}
