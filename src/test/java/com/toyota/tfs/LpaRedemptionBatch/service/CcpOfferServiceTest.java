package com.toyota.tfs.LpaRedemptionBatch.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.toyota.tfs.LpaRedemptionBatch.config.serviceurl.CcpServiceUrlConfig;
import com.toyota.tfs.LpaRedemptionBatch.model.ccp.CcpRequest;
import com.toyota.tfs.LpaRedemptionBatch.model.ccp.CcpResponse;
import com.toyota.tfs.LpaRedemptionBatch.model.ccp.CcpResponseItems;
import com.toyota.tfs.LpaRedemptionBatch.model.ccp.CcpResponseResultMessages;
import com.toyota.tfs.LpaRedemptionBatch.restclient.RestClient;

@ExtendWith(MockitoExtension.class)
class CcpOfferServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RestClient restClient;

    @Mock
    private MongoService mongoService;

    @Mock
    private CcpServiceUrlConfig ccpServiceUrlConfig;

    @InjectMocks
    private CcpOfferService ccpOfferService;

    @BeforeEach
    void setUp() {
        when(ccpServiceUrlConfig.getHost()).thenReturn("mock-host");
        when(ccpServiceUrlConfig.getContext()).thenReturn("/mock-context");
        when(ccpServiceUrlConfig.getUrl()).thenReturn("/mock-url");
        when(ccpServiceUrlConfig.getItem("val")).thenReturn("mock-program");
        when(ccpServiceUrlConfig.getAttr("val1")).thenReturn("mock-attr1");
        when(ccpServiceUrlConfig.getAttr("val2")).thenReturn("mock-attr2");
        when(ccpServiceUrlConfig.getAttr("val3")).thenReturn("mock-attr3");
        when(ccpServiceUrlConfig.getAttr("val4")).thenReturn("mock-attr4");
        when(ccpServiceUrlConfig.getAttr("val5")).thenReturn("mock-attr5");
    }

    @Test
    void testGetOfferDetails_Success() {
        // Arrange
        String accountNumber = "12345";
        String tenantId = "t002";

        CcpRequest mockRequest = new CcpRequest();
        CcpResponse mockResponse = new CcpResponse();
        mockResponse.setCcpResponseItems(new ArrayList<CcpResponseItems>());
        mockResponse.setCcpResponseResultMessages(new ArrayList<CcpResponseResultMessages>());

        when(restClient.getMuleSoftToken(tenantId)).thenReturn("mock-token");
        when(restTemplate.postForEntity(anyString(), any(), eq(CcpResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // Act
        CcpResponse response = ccpOfferService.getOfferDetails(accountNumber, tenantId);

        // Assert
        assertNotNull(response);
        assertEquals(0, response.getCcpResponseItems().size());
        verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(CcpResponse.class));
        verify(mongoService, times(1)).updateCcpDataToMongo(any(), eq(mockResponse), isNull(), eq(HttpStatus.OK));
    }

    @Test
    void testGetOfferDetails_Failure() {
        // Arrange
        String accountNumber = "12345";
        String tenantId = "t002";

        when(restClient.getMuleSoftToken(tenantId)).thenReturn("mock-token");
        when(restTemplate.postForEntity(anyString(), any(), eq(CcpResponse.class)))
                .thenThrow(new RuntimeException("Mock exception"));

        // Act
        CcpResponse response = ccpOfferService.getOfferDetails(accountNumber, tenantId);

        // Assert
        assertNull(response);
        verify(mongoService, times(1)).updateCcpDataToMongo(any(), isNull(), eq("Mock exception"), eq(HttpStatus.INTERNAL_SERVER_ERROR));
    }
}