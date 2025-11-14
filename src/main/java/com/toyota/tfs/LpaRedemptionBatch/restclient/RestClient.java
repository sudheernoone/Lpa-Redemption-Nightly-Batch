
package com.toyota.tfs.LpaRedemptionBatch.restclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tfs.core.svc.common.service.MuleSoftTokenServiceImpl;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@Setter
public class RestClient {
    @Autowired
    MuleSoftTokenServiceImpl muleSoftTokenService;

    @Value("${coresvc.factoryName}")
    private String factoryName;

    @Value("${coresvc.factoryCode}")
    private String factoryCode;

    @Value("${coresvc.tokenVersion}")
    private String tokenVersion;


    public String getMuleSoftToken(String tenantId) {
        log.info("label=DefiRestClient getMuleSoftToken()");
        return muleSoftTokenService.getCoreServiceMuleSoftToken(tenantId, factoryName, factoryCode, tokenVersion).getAccessToken();
    }
}