package com.toyota.tfs.LpaRedemptionBatch.exception;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.toyota.tfs.LpaRedemptionBatch.util.ErrorMessageHandler;

class LpaControllerAdviceTest {

    @InjectMocks
    private LpaControllerAdvice lpaControllerAdvice;

    @Mock
    private ErrorMessageHandler errorMessageHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void handleUnauthorizedException() {
        // Arrange
        UnauthorizedException exception = new UnauthorizedException("Unauthorized access");

        // Act
        ResponseEntity<?> response = lpaControllerAdvice.processUnauthorizedException(exception, null);

        // Assert
        assertNotNull(response);
    }
}