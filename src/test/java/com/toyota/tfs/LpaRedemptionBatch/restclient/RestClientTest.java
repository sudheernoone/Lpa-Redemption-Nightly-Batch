package com.toyota.tfs.LpaRedemptionBatch.restclient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tfs.core.svc.common.model.AuthorizedToken;
import com.tfs.core.svc.common.service.MuleSoftTokenServiceImpl;

class RestClientTest {

    @Mock
    private MuleSoftTokenServiceImpl muleSoftTokenService;

    @InjectMocks
    private RestClient restClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        restClient.setFactoryCode("CL14");
        restClient.setFactoryName("CoreService");
        restClient.setTokenVersion("V2");
        restClient.muleSoftTokenService= muleSoftTokenService;
    }

    @Test
    void getMuleSoftToken_Success() {
        // Arrange
        String tenantId = "t001";
        String expectedToken = "mockAccessToken";
        AuthorizedToken tokenResponse = new AuthorizedToken();
        tokenResponse.setAccessToken(expectedToken);

        when(muleSoftTokenService.getCoreServiceMuleSoftToken(
                eq(tenantId),
                anyString(),
                anyString(),
                anyString()
        )).thenReturn(tokenResponse);

        // Act
        String actualToken = restClient.getMuleSoftToken(tenantId);

        // Assert
        assertEquals(expectedToken, actualToken);
        verify(muleSoftTokenService, times(1)).getCoreServiceMuleSoftToken(
                eq(tenantId),
                anyString(),
                anyString(),
                anyString()
        );
    }

    @Test
    void getMuleSoftToken_Failure() {
        // Arrange
        String tenantId = "tenant1";
        when(muleSoftTokenService.getCoreServiceMuleSoftToken(
                eq(tenantId),
                anyString(),
                anyString(),
                anyString()
        )).thenThrow(new RuntimeException("Token retrieval failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> restClient.getMuleSoftToken(tenantId));
        assertEquals("Token retrieval failed", exception.getMessage());
        verify(muleSoftTokenService, times(1)).getCoreServiceMuleSoftToken(
                eq(tenantId),
                anyString(),
                anyString(),
                anyString()
        );
    }
}