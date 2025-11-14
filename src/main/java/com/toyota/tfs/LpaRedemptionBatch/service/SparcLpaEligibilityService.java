package com.toyota.tfs.LpaRedemptionBatch.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toyota.tfs.LpaRedemptionBatch.config.serviceurl.SparcServiceUrlConfig;
import com.toyota.tfs.LpaRedemptionBatch.model.dao.AccountHistory;
import com.toyota.tfs.LpaRedemptionBatch.model.dao.RedemptionHistory;
import com.toyota.tfs.LpaRedemptionBatch.model.dao.TenantDetails;
import com.toyota.tfs.LpaRedemptionBatch.model.sparc.DealerInfo;
import com.toyota.tfs.LpaRedemptionBatch.model.sparc.LpaQualifiers;
import com.toyota.tfs.LpaRedemptionBatch.model.sparc.SparcBadResponse;
import com.toyota.tfs.LpaRedemptionBatch.model.sparc.SparcRequest;
import com.toyota.tfs.LpaRedemptionBatch.model.sparc.SparcResponse;
import com.toyota.tfs.LpaRedemptionBatch.model.sparc.VehicleInfo;
import com.toyota.tfs.LpaRedemptionBatch.repository.TenantDetailsRepository;
import com.toyota.tfs.LpaRedemptionBatch.restclient.RestClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SparcLpaEligibilityService {

    @Autowired
    MongoService mongoService;
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RestClient restClient;
    private static final String HTTPS = "https://";
    @Autowired
    TenantDetailsRepository tenantDetailsRepository;

    @Autowired
    private SparcServiceUrlConfig sparcServiceUrlConfig;
    @Value("${api.tfs.sparc.util.clientId}")
    private String sparcClientId;
    @Value("${api.tfs.sparc.util.requestType}")
    private String sparcRequestType;


    public SparcResponse getLpaEligibility(AccountHistory accountHistory, AccountHistory newContract,
                                           RedemptionHistory redemptionHistory, String tenantId) throws Exception {

        SparcResponse sparcResponse;
        SparcRequest sparcRequest = new SparcRequest();
        SparcBadResponse sparcBadResponse;

        try {
            SimpleDateFormat sdfDB = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfAPI = new SimpleDateFormat("MM/dd/yyyy");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            /*Fetch Token*/
            headers.setBearerAuth(restClient.getMuleSoftToken(tenantId));

            TenantDetails tenantDetails = tenantDetailsRepository.getTenantDetails(redemptionHistory.getTenantId());
            
            sparcRequest.setTenantId(newContract.getTenantId());
            sparcRequest.setSenderId(tenantDetails.getSparcSystemId());
            sparcRequest.setClientId(sparcClientId);
            sparcRequest.setRequestType(sparcRequestType);

            LpaQualifiers lpaQualifiers = new LpaQualifiers();
            lpaQualifiers.setProduct(newContract.getProductType());
            lpaQualifiers.setFinanceProgramType(newContract.getFinProgramType());
            lpaQualifiers.setNewVehicleFlag(true);

            lpaQualifiers.setContractDate(sdfAPI.format(sdfDB.parse(newContract.getContractStartDate())));
            lpaQualifiers.setMaturityDate(sdfAPI.format(sdfDB.parse(accountHistory.getCurrentMaturityDate())));
            lpaQualifiers.setReturnDate(sdfAPI.format(sdfDB.parse(redemptionHistory.getReturnDate())));
            lpaQualifiers.setGroundingDate(sdfAPI.format(sdfDB.parse(redemptionHistory.getGroundingDate())));

            if (accountHistory.getNoOfPaymentsRemaining() != null
                    && !accountHistory.getNoOfPaymentsRemaining().equalsIgnoreCase("")) {
                lpaQualifiers.setNumberOfPaymentsRemaining(Long.parseLong(accountHistory.getNoOfPaymentsRemaining()));
            }

            lpaQualifiers.setTier(newContract.getTier());
            lpaQualifiers.setTerm(newContract.getCurrentTerm());

            if (newContract.getGroupAreaId() != null
                    && !newContract.getGroupAreaId().equalsIgnoreCase("")) {
                lpaQualifiers.setGroupAreaId(Long.parseLong(newContract.getGroupAreaId()));
            }

            DealerInfo dealerInfo = new DealerInfo();
            dealerInfo.setDealerId(newContract.getDealerNumber());

            VehicleInfo returningVehicleInfo = new VehicleInfo();
            returningVehicleInfo.setAccessoryCode(new ArrayList<String>());
            returningVehicleInfo.setMake(accountHistory.getVehicleMake());
            returningVehicleInfo.setModelCode(accountHistory.getVehicleModelCode());
            returningVehicleInfo.setModelYear(accountHistory.getVehicleModelYear());
            returningVehicleInfo.setVehicleCondition(accountHistory.getVehicleCondition());

            VehicleInfo newVehicleInfo = new VehicleInfo();
            newVehicleInfo.setAccessoryCode(new ArrayList<String>());
            newVehicleInfo.setMake(newContract.getVehicleMake());
            newVehicleInfo.setModelCode(newContract.getVehicleModelCode());
            newVehicleInfo.setModelYear(newContract.getVehicleModelYear());
            newVehicleInfo.setVehicleCondition(newContract.getVehicleCondition());

            lpaQualifiers.setNewVehicleInfo(newVehicleInfo);
            lpaQualifiers.setReturningVehicleInfo(returningVehicleInfo);
            lpaQualifiers.setDealerInfo(dealerInfo);

            List<LpaQualifiers> lpaQualifiersList = new ArrayList<LpaQualifiers>();
            lpaQualifiersList.add(lpaQualifiers);
            sparcRequest.setLpaQualifiers(lpaQualifiersList);

            String host = sparcServiceUrlConfig.getHost();
            String context = sparcServiceUrlConfig.getContext();
            String url = sparcServiceUrlConfig.getUrl();
            String sparcApiUrl=HTTPS+host+context+url;

            HttpEntity<SparcRequest> httpEntity = new HttpEntity<>(sparcRequest, headers);

            ResponseEntity<SparcResponse> responseEntity = restTemplate.postForEntity(sparcApiUrl, httpEntity,
                    SparcResponse.class);
            sparcResponse = responseEntity.getBody();
            HttpStatus status = (HttpStatus) responseEntity.getStatusCode();

            mongoService.updateSparcDataToMongo(sparcRequest, sparcResponse, null,
                    null, status);

            if (status == HttpStatus.OK) {
                return sparcResponse;
            } else {
                return null;
            }

        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            log.error("#ServiceError - HttpRequestMethodNotSupportedException: " + e.getMessage());
            if (e.getMessage() != null && e.getMessage().startsWith("400")) {
                ObjectMapper oMp = new ObjectMapper();
                String str = e.getMessage().split(": ")[1];
                sparcBadResponse = oMp.readValue(str.substring(1, str.length() - 1), SparcBadResponse.class);
                mongoService.updateSparcDataToMongo(sparcRequest, null, sparcBadResponse,
                        null, HttpStatus.BAD_REQUEST);
            } else {
                mongoService.updateSparcDataToMongo(sparcRequest, null, null,
                        e.getMessage(), HttpStatus.BAD_REQUEST);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            mongoService.updateSparcDataToMongo(sparcRequest, null, null,
                    e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return null;
        }
    }
}
