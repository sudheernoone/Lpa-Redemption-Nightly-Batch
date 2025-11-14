package com.toyota.tfs.LpaRedemptionBatch.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.toyota.tfs.LpaRedemptionBatch.config.serviceurl.SparcServiceUrlConfig;
import com.toyota.tfs.LpaRedemptionBatch.model.dao.AccountHistory;
import com.toyota.tfs.LpaRedemptionBatch.model.dao.RedemptionHistory;
import com.toyota.tfs.LpaRedemptionBatch.model.dao.TenantDetails;
import com.toyota.tfs.LpaRedemptionBatch.model.sparc.SparcRequest;
import com.toyota.tfs.LpaRedemptionBatch.model.sparc.SparcResponse;
import com.toyota.tfs.LpaRedemptionBatch.repository.TenantDetailsRepository;
import com.toyota.tfs.LpaRedemptionBatch.restclient.RestClient;

@ExtendWith(MockitoExtension.class)
class SparcLpaEligibilityServiceTest {

    @Mock
    private MongoService mongoService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RestClient restClient;

    @Mock
    private TenantDetailsRepository tenantDetailsRepository;

    @Mock
    private SparcServiceUrlConfig sparcServiceUrlConfig;

    @InjectMocks
    private SparcLpaEligibilityService sparcLpaEligibilityService;

    @BeforeEach
    void setUp() {
        when(sparcServiceUrlConfig.getHost()).thenReturn("test-host");
        when(sparcServiceUrlConfig.getContext()).thenReturn("/test-context");
        when(sparcServiceUrlConfig.getUrl()).thenReturn("/test-url");
    }

    @Test
    void testGetLpaEligibility_Success() throws Exception {
        // Arrange
        AccountHistory accountHistory = new AccountHistory();
        accountHistory.setCurrentMaturityDate("2023-12-31");
        accountHistory.setVehicleMake("Toyota");
        accountHistory.setVehicleModelCode("Camry");
        accountHistory.setVehicleModelYear("2023");
        accountHistory.setVehicleCondition("New");

        AccountHistory newContract = new AccountHistory();
        newContract.setTenantId("tenant123");
        newContract.setProductType("Lease");
        newContract.setFinProgramType("Standard");
        newContract.setContractStartDate("2023-01-01");
        newContract.setCurrentTerm(36+"");
        newContract.setTier("A");
        newContract.setDealerNumber("D123");

        RedemptionHistory redemptionHistory = new RedemptionHistory();
        redemptionHistory.setTenantId("tenant123");
        redemptionHistory.setReturnDate("2023-11-01");
        redemptionHistory.setGroundingDate("2023-10-01");

        TenantDetails tenantDetails = new TenantDetails();
        tenantDetails.setSparcSystemId("system123");

        when(tenantDetailsRepository.getTenantDetails("tenant123")).thenReturn(tenantDetails);
        when(restClient.getMuleSoftToken("tenant123")).thenReturn("test-token");

        SparcResponse sparcResponse = new SparcResponse();
        ResponseEntity<SparcResponse> responseEntity = new ResponseEntity<>(sparcResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(), eq(SparcResponse.class))).thenReturn(responseEntity);

        // Act
        SparcResponse result = sparcLpaEligibilityService.getLpaEligibility(accountHistory, newContract, redemptionHistory, "tenant123");

        // Assert
        assertNotNull(result);
        verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(SparcResponse.class));
        verify(mongoService, times(1)).updateSparcDataToMongo(any(SparcRequest.class), eq(sparcResponse), isNull(), isNull(), eq(HttpStatus.OK));
    }

    @Test
    void testGetLpaEligibility_Fail() throws Exception {
        // Arrange
        AccountHistory accountHistory = new AccountHistory();
        accountHistory.setCurrentMaturityDate("2023-12-31");
        accountHistory.setVehicleMake("Toyota");
        accountHistory.setVehicleModelCode("Camry");
        accountHistory.setVehicleModelYear("2023");
        accountHistory.setVehicleCondition("New");

        AccountHistory newContract = new AccountHistory();
        newContract.setTenantId("tenant123");
        newContract.setProductType("Lease");
        newContract.setFinProgramType("Standard");
        newContract.setContractStartDate("2023-01-01");
        newContract.setCurrentTerm(36+"");
        newContract.setTier("A");
        newContract.setDealerNumber("D123");

        RedemptionHistory redemptionHistory = new RedemptionHistory();
        redemptionHistory.setTenantId("tenant123");
        redemptionHistory.setReturnDate("2023-11-01");
        redemptionHistory.setGroundingDate("2023-10-01");

        TenantDetails tenantDetails = new TenantDetails();
        tenantDetails.setSparcSystemId("system123");

        when(tenantDetailsRepository.getTenantDetails("tenant123")).thenReturn(tenantDetails);
        when(restClient.getMuleSoftToken("tenant123")).thenReturn("test-token");

        SparcResponse sparcResponse = new SparcResponse();
        ResponseEntity<SparcResponse> responseEntity = new ResponseEntity<>(sparcResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        when(restTemplate.postForEntity(anyString(), any(), eq(SparcResponse.class))).thenReturn(responseEntity);

        // Act
        SparcResponse result = sparcLpaEligibilityService.getLpaEligibility(accountHistory, newContract, redemptionHistory, "tenant123");

        // Assert
        //assertNotNull(result);
        //verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(SparcResponse.class));
        //verify(mongoService, times(1)).updateSparcDataToMongo(any(SparcRequest.class), eq(sparcResponse), isNull(), isNull(), eq(HttpStatus.OK));
    }

    @Test
    void testGetLpaEligibility_BadRequest() throws Exception {
        // Arrange
        AccountHistory accountHistory = new AccountHistory();
        accountHistory.setCurrentMaturityDate("2023-12-31");
        accountHistory.setVehicleMake("Toyota");
        accountHistory.setVehicleModelCode("Camry");
        accountHistory.setVehicleModelYear("2023");
        accountHistory.setVehicleCondition("New");

        AccountHistory newContract = new AccountHistory();
        newContract.setTenantId("tenant123");
        newContract.setProductType("Lease");
        newContract.setFinProgramType("Standard");
        newContract.setContractStartDate("2023-01-01");
        newContract.setCurrentTerm(36+"");
        newContract.setTier("A");
        newContract.setDealerNumber("D123");

        RedemptionHistory redemptionHistory = new RedemptionHistory();
        redemptionHistory.setTenantId("tenant123");
        redemptionHistory.setReturnDate("2023-11-01");
        redemptionHistory.setGroundingDate("2023-10-01");

        TenantDetails tenantDetails = new TenantDetails();
        tenantDetails.setSparcSystemId("system123");

        when(restClient.getMuleSoftToken("tenant123")).thenReturn("test-token");


        when(tenantDetailsRepository.getTenantDetails("tenant123")).thenReturn(tenantDetails);

        // Use valid JSON in the exception message
        when(restTemplate.postForEntity(anyString(), any(), eq(SparcResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, " 400 Bad Request on POST request for \"https://internal-apigateway-stg.toyotafintech.io/defi-services/NPAU_UAT00_AAE/V1/SaveLPAWaiverRedemption\": \"{\"CorrelationId\":\"7c159d67-963f-4fa5-b744-28792d06c37f\",\"Errors\":[{\"ErrorCode\":\"400\",\"Description\":\"The account must be in Grounding Confirmed\"}]}\""));

        // Act
        SparcResponse result = sparcLpaEligibilityService.getLpaEligibility(accountHistory, newContract, redemptionHistory, "tenant123");

        // Assert
        assertNull(result);
        //verify(mongoService, times(1)).updateSparcDataToMongo(any(SparcRequest.class), isNull(), any(SparcBadResponse.class), null, eq(HttpStatus.BAD_REQUEST));
    }

    @Test
    void testGetLpaEligibility_BadRequest1() throws Exception {
        // Arrange
        AccountHistory accountHistory = new AccountHistory();
        accountHistory.setCurrentMaturityDate("2023-12-31");
        accountHistory.setVehicleMake("Toyota");
        accountHistory.setVehicleModelCode("Camry");
        accountHistory.setVehicleModelYear("2023");
        accountHistory.setVehicleCondition("New");
        accountHistory.setNoOfPaymentsRemaining("3");

        AccountHistory newContract = new AccountHistory();
        newContract.setTenantId("tenant123");
        newContract.setProductType("Lease");
        newContract.setFinProgramType("Standard");
        newContract.setContractStartDate("2023-01-01");
        newContract.setCurrentTerm(36+"");
        newContract.setTier("A");
        newContract.setNoOfPaymentsRemaining("3");
        newContract.setGroupAreaId("1");
        newContract.setDealerNumber("D123");

        RedemptionHistory redemptionHistory = new RedemptionHistory();
        redemptionHistory.setTenantId("tenant123");
        redemptionHistory.setReturnDate("2023-11-01");
        redemptionHistory.setGroundingDate("2023-10-01");

        TenantDetails tenantDetails = new TenantDetails();
        tenantDetails.setSparcSystemId("system123");

        when(restClient.getMuleSoftToken("tenant123")).thenReturn("test-token");


        when(tenantDetailsRepository.getTenantDetails("tenant123")).thenReturn(tenantDetails);

        // Use valid JSON in the exception message
        when(restTemplate.postForEntity(anyString(), any(), eq(SparcResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, " Request on POST request for \"https://internal-apigateway-stg.toyotafintech.io/defi-services/NPAU_UAT00_AAE/V1/SaveLPAWaiverRedemption\": \"{\"CorrelationId\":\"7c159d67-963f-4fa5-b744-28792d06c37f\",\"Errors\":[{\"ErrorCode\":\"400\",\"Description\":\"The account must be in Grounding Confirmed\"}]}\""));

        // Act
        SparcResponse result = sparcLpaEligibilityService.getLpaEligibility(accountHistory, newContract, redemptionHistory, "tenant123");

        // Assert
        assertNull(result);
        //verify(mongoService, times(1)).updateSparcDataToMongo(any(SparcRequest.class), isNull(), any(SparcBadResponse.class), null, eq(HttpStatus.BAD_REQUEST));
    }
}