package com.toyota.tfs.LpaRedemptionBatch.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.toyota.tfs.LpaRedemptionBatch.config.serviceurl.CcpServiceUrlConfig;
import com.toyota.tfs.LpaRedemptionBatch.model.ccp.CcpRequest;
import com.toyota.tfs.LpaRedemptionBatch.model.ccp.CcpRequestAttribute;
import com.toyota.tfs.LpaRedemptionBatch.model.ccp.CcpRequestCondition;
import com.toyota.tfs.LpaRedemptionBatch.model.ccp.CcpRequestConditionAttribute;
import com.toyota.tfs.LpaRedemptionBatch.model.ccp.CcpRequestConditionSet;
import com.toyota.tfs.LpaRedemptionBatch.model.ccp.CcpRequestConditionValue;
import com.toyota.tfs.LpaRedemptionBatch.model.ccp.CcpRequestContext;
import com.toyota.tfs.LpaRedemptionBatch.model.ccp.CcpResponse;
import com.toyota.tfs.LpaRedemptionBatch.restclient.RestClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CcpOfferService {

    @Autowired
    Environment env;
    @Autowired
    MongoService mongoService;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    RestClient restClient;
    private static final String HTTPS = "https://";

    @Autowired
    private CcpServiceUrlConfig ccpServiceUrlConfig;


    public CcpResponse getOfferDetails(String accountNumber, String tenantId) {

        CcpRequest ccpRequest = new CcpRequest();
        CcpResponse ccpResponse = null;
        List<String> accountNum = new ArrayList<>();
        accountNum.add(accountNumber);

        List<String> programName = new ArrayList<>();
        programName.add(ccpServiceUrlConfig.getItem("val"));

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            /*Fetch Token*/
            headers.setBearerAuth(restClient.getMuleSoftToken(tenantId));


            headers.set("x-tenant-id", "t002");

            //Set RequestContext
            CcpRequestContext ccpRequestContext = new CcpRequestContext();

            CcpRequestAttribute ccpRequestAttribute1 = new CcpRequestAttribute();
            ccpRequestAttribute1.setKey(ccpServiceUrlConfig.getAttr("val1"));
            CcpRequestAttribute ccpRequestAttribute2 = new CcpRequestAttribute();
            ccpRequestAttribute2.setKey(ccpServiceUrlConfig.getAttr("val2"));
            CcpRequestAttribute ccpRequestAttribute3 = new CcpRequestAttribute();
            ccpRequestAttribute3.setKey(ccpServiceUrlConfig.getAttr("val3"));
            CcpRequestAttribute ccpRequestAttribute4 = new CcpRequestAttribute();
            ccpRequestAttribute4.setKey(ccpServiceUrlConfig.getAttr("val4"));
            CcpRequestAttribute ccpRequestAttribute5 = new CcpRequestAttribute();
            ccpRequestAttribute5.setKey(ccpServiceUrlConfig.getAttr("val5"));

            List<CcpRequestAttribute> ccpRequestAttributes = new ArrayList<>();
            ccpRequestAttributes.add(ccpRequestAttribute1);
            ccpRequestAttributes.add(ccpRequestAttribute2);
            ccpRequestAttributes.add(ccpRequestAttribute3);
            ccpRequestAttributes.add(ccpRequestAttribute4);
            ccpRequestAttributes.add(ccpRequestAttribute5);

            ccpRequestContext.setCcpRequestAttribute(ccpRequestAttributes);
            ccpRequest.setCcpRequestContext(ccpRequestContext);


            //Set RequestConditionSet
            CcpRequestConditionSet ccpRequestConditionSet = new CcpRequestConditionSet();
            ccpRequestConditionSet.setOperator("And");

            CcpRequestCondition ccpRequestCondition1 = new CcpRequestCondition();
            CcpRequestConditionAttribute ccpRequestConditionAttribute1 = new CcpRequestConditionAttribute();
            ccpRequestConditionAttribute1.setKey(ccpServiceUrlConfig.getAttr("val5"));
            ccpRequestCondition1.setCcpRequestConditionAttribute(ccpRequestConditionAttribute1);
            ccpRequestCondition1.setOperator("Equals");
            CcpRequestConditionValue CcpRequestConditionValue1 = new CcpRequestConditionValue();
            CcpRequestConditionValue1.setItems(accountNum);
            ccpRequestCondition1.setCcpRequestConditionValue(CcpRequestConditionValue1);

            CcpRequestCondition ccpRequestCondition2 = new CcpRequestCondition();
            CcpRequestConditionAttribute ccpRequestConditionAttribute2 = new CcpRequestConditionAttribute();
            ccpRequestConditionAttribute2.setKey(ccpServiceUrlConfig.getAttr("val1"));
            ccpRequestCondition2.setCcpRequestConditionAttribute(ccpRequestConditionAttribute2);
            ccpRequestCondition2.setOperator("Equals");
            CcpRequestConditionValue CcpRequestConditionValue2 = new CcpRequestConditionValue();
            CcpRequestConditionValue2.setItems(programName);
            ccpRequestCondition2.setCcpRequestConditionValue(CcpRequestConditionValue2);

            
            List<CcpRequestCondition> ccpRequestConditions = new ArrayList<>();
            ccpRequestConditions.add(ccpRequestCondition1);
            ccpRequestConditions.add(ccpRequestCondition2);

            ccpRequestConditionSet.setCcpRequestCondition(ccpRequestConditions);
            ccpRequest.setCcpRequestConditionSet(ccpRequestConditionSet);

            HttpEntity<CcpRequest> httpEntity = new HttpEntity<>(ccpRequest, headers);

            String host = ccpServiceUrlConfig.getHost();
            String context = ccpServiceUrlConfig.getContext();
            String url = ccpServiceUrlConfig.getUrl();
            String ccpApiUrl=HTTPS+host+context+url;

            ResponseEntity<CcpResponse> responseEntity = restTemplate.postForEntity(ccpApiUrl, httpEntity, CcpResponse.class);
            ccpResponse = responseEntity.getBody();

            HttpStatus status = (HttpStatus) responseEntity.getStatusCode();
            mongoService.updateCcpDataToMongo(ccpRequest, ccpResponse, null, status);

            if (status == HttpStatus.OK) {
                return ccpResponse;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            mongoService.updateCcpDataToMongo(ccpRequest, null, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return null;
        }
    }
}

