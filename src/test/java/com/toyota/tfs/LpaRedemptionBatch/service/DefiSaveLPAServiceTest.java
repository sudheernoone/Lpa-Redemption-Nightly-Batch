package com.toyota.tfs.LpaRedemptionBatch.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.toyota.tfs.LpaRedemptionBatch.config.serviceurl.DefiServiceUrlConfig;
import com.toyota.tfs.LpaRedemptionBatch.model.dao.RedemptionHistory;
import com.toyota.tfs.LpaRedemptionBatch.model.defi.DefiRequest;
import com.toyota.tfs.LpaRedemptionBatch.model.defi.DefiResponse;
import com.toyota.tfs.LpaRedemptionBatch.restclient.RestClient;
import com.toyota.tfs.LpaRedemptionBatch.util.LpaDefiUtils;

@ExtendWith(MockitoExtension.class)
class DefiSaveLPAServiceTest {

    @Mock
    private MongoService mongoService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private LpaDefiUtils lpaDefiUtils;

    @Mock
    private RestClient restClient;

    @Mock
    private DefiServiceUrlConfig defiServiceUrlConfig;

    @InjectMocks
    private DefiSaveLPAService defiSaveLPAService;

    private RedemptionHistory redemptionHistory;
    private DefiRequest defiRequest;
    private DefiResponse defiResponse;

    @BeforeEach
    void setUp() {
        redemptionHistory = new RedemptionHistory();
        redemptionHistory.setTenantId("tenant123");
        redemptionHistory.setAccountNumber("account123");

        defiRequest = new DefiRequest();
        defiResponse = new DefiResponse();
        defiResponse.setHasErrors(false);

        when(defiServiceUrlConfig.getHost()).thenReturn("api.defi.com");
        when(defiServiceUrlConfig.getContext()).thenReturn("/context");
        when(defiServiceUrlConfig.getUrl()).thenReturn("/process");
    }

    @Test
    void testProcessTransaction_Success() throws Exception {
        // Arrange
        String tenantId = "tenant123";
        String token = "mockToken";
        when(restClient.getMuleSoftToken(tenantId)).thenReturn(token);
        when(lpaDefiUtils.prepareDefiRequest(redemptionHistory)).thenReturn(defiRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        ResponseEntity<DefiResponse> responseEntity = new ResponseEntity<>(defiResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(), eq(DefiResponse.class))).thenReturn(responseEntity);

        // Act
        DefiResponse result = defiSaveLPAService.processTransaction(redemptionHistory, tenantId);

        // Assert
        assertNotNull(result);
        assertFalse(result.isHasErrors());
        verify(mongoService, times(1)).updateDefiDataToMongo(defiRequest, defiResponse, null, HttpStatus.OK);
    }
    @Test
    void testProcessTransaction_Error() throws Exception {
        // Arrange
        String tenantId = "tenant123";
        String token = "mockToken";
        when(restClient.getMuleSoftToken(tenantId)).thenReturn(token);
        when(lpaDefiUtils.prepareDefiRequest(redemptionHistory)).thenReturn(defiRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        ResponseEntity<DefiResponse> responseEntity = new ResponseEntity<>(defiResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(), eq(DefiResponse.class))).thenReturn(responseEntity);
        responseEntity.getBody().setHasErrors(true);
        com.toyota.tfs.LpaRedemptionBatch.model.defi.Error error = new com.toyota.tfs.LpaRedemptionBatch.model.defi.Error();
        error.setDescription("Sample error");
        responseEntity.getBody().setErrors(java.util.Collections.singletonList(error));
        // Act
        DefiResponse result = defiSaveLPAService.processTransaction(redemptionHistory, tenantId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isHasErrors());
        verify(mongoService, times(1)).updateDefiDataToMongo(defiRequest, defiResponse, null, HttpStatus.OK);
    }

    @Test
    void testProcessTransaction_HttpClientErrorException() throws Exception {
        // Arrange
        String tenantId = "tenant123";
        String token = "mockToken";
        when(restClient.getMuleSoftToken(tenantId)).thenReturn(token);
        when(lpaDefiUtils.prepareDefiRequest(redemptionHistory)).thenReturn(defiRequest);

        when(restTemplate.postForEntity(anyString(), any(), eq(DefiResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request on POST request for \"https://internal-apigateway-stg.toyotafintech.io/defi-services/NPAU_UAT00_AAE/V1/SaveLPAWaiverRedemption\": \"{\"ActivityId\":\"7c159d67-963f-4fa5-b744-28792d06c37f\",\"Errors\":[{\"ErrorCode\":\"400\",\"Description\":\"The account must be in Grounding Confirmed\"}]}\""));

        // Act
        DefiResponse result = defiSaveLPAService.processTransaction(redemptionHistory, tenantId);

        // Assert
        assertNull(result);
    }

    @Test
    void testProcessTransaction_Exception() throws Exception {
        // Arrange
        String tenantId = "tenant123";
        String token = "mockToken";
        when(restClient.getMuleSoftToken(tenantId)).thenReturn(token);
        when(lpaDefiUtils.prepareDefiRequest(redemptionHistory)).thenReturn(defiRequest);

        when(restTemplate.postForEntity(anyString(), any(), eq(DefiResponse.class)))
                .thenThrow(new RuntimeException("Internal Server Error"));

        // Act
        DefiResponse result = defiSaveLPAService.processTransaction(redemptionHistory, tenantId);

        // Assert
        assertNull(result);
        verify(mongoService, times(1)).updateDefiDataToMongo(defiRequest, null, "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}