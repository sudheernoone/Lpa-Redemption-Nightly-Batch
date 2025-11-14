package com.toyota.tfs.LpaRedemptionBatch.util;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.toyota.tfs.LpaRedemptionBatch.config.OAuthUrlConfig;
import com.toyota.tfs.LpaRedemptionBatch.exception.UnauthorizedException;
import com.toyota.tfs.LpaRedemptionBatch.model.token.TokenResponse;

class TokenValidatorTest {

    @Mock
    private OAuthUrlConfig oAuthUrlConfig;

    @Mock
    private ErrorMessageHandler errorMessageHandler;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TokenValidator tokenValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validateToken_ValidToken() throws UnauthorizedException {
        // Arrange
        String token = "Bearer validToken";
        String serviceUrl = "https://example.com/token/validate";
        TokenResponse tokenResponse = new TokenResponse();

        when(oAuthUrlConfig.getHost()).thenReturn("example.com");
        when(oAuthUrlConfig.getContext()).thenReturn("/token");
        when(oAuthUrlConfig.getUrl()).thenReturn("/validate");
        when(restTemplate.exchange(eq(serviceUrl), eq(HttpMethod.GET), any(HttpEntity.class), eq(TokenResponse.class)))
                .thenReturn(ResponseEntity.ok(tokenResponse));

        // Act
        tokenValidator.validateToken(token);

        // Assert
        verify(restTemplate, times(1)).exchange(eq(serviceUrl), eq(HttpMethod.GET), any(HttpEntity.class), eq(TokenResponse.class));
    }

    @Test
    void validateToken_InvalidToken_() {
        // Arrange
        String token = "Bearer invalidToken";
        String serviceUrl = "https://example.com/token/validate";

        ResponseEntity<TokenResponse> validateTokenResponse = new ResponseEntity<>(null, HttpHeaders.EMPTY,     401);

        when(oAuthUrlConfig.getHost()).thenReturn("example.com");
        when(oAuthUrlConfig.getContext()).thenReturn("/token");
        when(oAuthUrlConfig.getUrl()).thenReturn("/validate");
        when(restTemplate.exchange(eq(serviceUrl), eq(HttpMethod.GET), any(HttpEntity.class), eq(TokenResponse.class)))
                .thenReturn(validateTokenResponse);

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> tokenValidator.validateToken(token));
        verify(restTemplate, times(1)).exchange(eq(serviceUrl), eq(HttpMethod.GET), any(HttpEntity.class), eq(TokenResponse.class));
    }
    @Test
    void validateToken_InvalidToken_WithException() {
        // Arrange
        String token = "Bearer invalidToken";
        String serviceUrl = "https://example.com/token/validate";

        when(oAuthUrlConfig.getHost()).thenReturn("example.com");
        when(oAuthUrlConfig.getContext()).thenReturn("/token");
        when(oAuthUrlConfig.getUrl()).thenReturn("/validate");
        when(restTemplate.exchange(eq(serviceUrl), eq(HttpMethod.GET), any(HttpEntity.class), eq(TokenResponse.class)))
                .thenThrow(new RuntimeException(new UnauthorizedException("INVALID_TOKEN", "Invalid token")));

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> tokenValidator.validateToken(token));
        verify(restTemplate, times(1)).exchange(eq(serviceUrl), eq(HttpMethod.GET), any(HttpEntity.class), eq(TokenResponse.class));
    }

    @Test
    void validateToken_MissingToken() {
        // Arrange
        String token = "";

        when(errorMessageHandler.getErrorMessage(any())).thenReturn("No token provided");

        // Act & Assert
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> tokenValidator.validateToken(token));
        verify(errorMessageHandler, times(1)).getErrorMessage(any());
        assert exception.getMessage().contains("No token provided");
    }
}