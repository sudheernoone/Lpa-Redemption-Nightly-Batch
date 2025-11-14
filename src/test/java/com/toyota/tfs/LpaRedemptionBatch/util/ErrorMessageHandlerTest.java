package com.toyota.tfs.LpaRedemptionBatch.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;

class ErrorMessageHandlerTest {

    @Mock
    private Environment env;

    @InjectMocks
    private ErrorMessageHandler errorMessageHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetErrorMessage() {
        // Arrange
        String errorCode = "ERRLPAR001";
        String expectedMessage = "Invalid grounding status";
        when(env.getProperty(errorCode)).thenReturn(expectedMessage);

        // Act
        String actualMessage = errorMessageHandler.getErrorMessage(errorCode);

        // Assert
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testGetErrorMessage_Null() {
        // Arrange
        String errorCode = "ERRLPAR999";
        when(env.getProperty(errorCode)).thenReturn(null);

        // Act
        String actualMessage = errorMessageHandler.getErrorMessage(errorCode);

        // Assert
        assertEquals(null, actualMessage);
    }
}