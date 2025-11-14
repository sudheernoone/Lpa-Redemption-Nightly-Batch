package com.toyota.tfs.LpaRedemptionBatch.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class AccServiceExceptionTest {

    @Test
    void testDefaultConstructor() {
        // Act
        AccServiceException exception = new AccServiceException();

        // Assert
        assertNull(exception.getErrCode());
        assertNull(exception.getErrMsg());
        assertNull(exception.getMessage());
    }

    @Test
    void testConstructorWithMessage() {
        // Arrange
        String message = "Test error message";

        // Act
        AccServiceException exception = new AccServiceException(message);

        // Assert
        assertNull(exception.getErrCode());
        assertEquals(message, exception.getErrMsg());
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testConstructorWithErrorCodeAndMessage() {
        // Arrange
        String errorCode = "ERR001";
        String errorMessage = "Test error message";

        // Act
        AccServiceException exception = new AccServiceException(errorCode, errorMessage);

        // Assert
        assertEquals(errorCode, exception.getErrCode());
        assertEquals(errorMessage, exception.getErrMsg());
        assertEquals(errorMessage, exception.getMessage());
    }
}