package com.toyota.tfs.LpaRedemptionBatch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toyota.tfs.LpaRedemptionBatch.config.serviceurl.DefiServiceUrlConfig;
import com.toyota.tfs.LpaRedemptionBatch.model.dao.RedemptionHistory;
import com.toyota.tfs.LpaRedemptionBatch.model.defi.DefiRequest;
import com.toyota.tfs.LpaRedemptionBatch.model.defi.DefiResponse;
import com.toyota.tfs.LpaRedemptionBatch.model.defi.Error;
import com.toyota.tfs.LpaRedemptionBatch.restclient.RestClient;
import com.toyota.tfs.LpaRedemptionBatch.util.LpaDefiUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DefiSaveLPAService {

    @Autowired
    MongoService mongoService;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    LpaDefiUtils lpaDefiUtils;

    @Autowired
    RestClient restClient;

    private static final String HTTPS = "https://";

    @Autowired
    private DefiServiceUrlConfig defiServiceUrlConfig;


    public DefiResponse processTransaction(RedemptionHistory redemptionHistory, String tenantId) throws Exception {

        String err = "";
        DefiResponse defiResponse = null;
        HttpStatus status = null;
        DefiRequest defiRequest = null;
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            /*Fetch Token*/
            headers.setBearerAuth(restClient.getMuleSoftToken(tenantId));

            defiRequest = lpaDefiUtils.prepareDefiRequest(redemptionHistory);
            log.info("defi request : " + defiRequest);

            HttpEntity<DefiRequest> httpEntity = new HttpEntity<>(defiRequest, headers);

            String host = defiServiceUrlConfig.getHost();
            String context = defiServiceUrlConfig.getContext();
            String url = defiServiceUrlConfig.getUrl();
            String defiApiUrl=HTTPS+host+context+url;

            ResponseEntity<DefiResponse> responseEntity = restTemplate.postForEntity(defiApiUrl, httpEntity,
                    DefiResponse.class);

            defiResponse = responseEntity.getBody();
            log.info("defi response : " + defiResponse);

            status = (HttpStatus) responseEntity.getStatusCode();
            mongoService.updateDefiDataToMongo(defiRequest, defiResponse, null, status);

            if (status == HttpStatus.OK) {
                if (defiResponse.isHasErrors()) {
                    for (Error error : defiResponse.getErrors()) {
                        err += error.getDescription();
                    }
                    log.info("defi Error: " + err);
                }
            }
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            log.error("#ServiceError - HttpRequestMethodNotSupportedException: " + e.getMessage());
            if (e.getMessage() != null && e.getMessage().startsWith("400 Bad Request")) {
                ObjectMapper oMp = new ObjectMapper();
                String str = e.getMessage().split(": ")[1];
                defiResponse = oMp.readValue(str.substring(1, str.length() - 1), DefiResponse.class);

                mongoService.updateDefiDataToMongo(defiRequest, defiResponse,
                        null, HttpStatus.BAD_REQUEST);
            } else {
                mongoService.updateDefiDataToMongo(defiRequest, null, e.getMessage(), HttpStatus.BAD_REQUEST);
            }
            defiResponse = null;
        } catch (Exception e) {
            e.printStackTrace();
            mongoService.updateDefiDataToMongo(defiRequest, null, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            defiResponse = null;
        }
        return defiResponse;
    }

}
