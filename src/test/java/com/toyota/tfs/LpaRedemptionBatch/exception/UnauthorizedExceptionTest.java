package com.toyota.tfs.LpaRedemptionBatch.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class UnauthorizedExceptionTest {

    @Test
    void testDefaultConstructor() {
        // Act
        UnauthorizedException exception = new UnauthorizedException();

        // Assert
        assertNull(exception.getMessage());
    }

    @Test
    void testConstructorWithMessage() {
        // Arrange
        String message = "Test error message";

        // Act
        UnauthorizedException exception = new UnauthorizedException(message);

        // Assert
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testConstructorWithErrorCodeAndMessage() {
        // Arrange
        String errorCode = "ERR001";
        String errorMessage = "Test error message";

        // Act
        UnauthorizedException exception = new UnauthorizedException(errorCode, errorMessage);

        // Assert
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testConstructorWithMessageAndThrowableCause() {
        // Arrange
        String message = "Test error message";

        // Act
        UnauthorizedException exception = new UnauthorizedException(message,new Throwable());

        // Assert
        assertNotNull(exception);
    }

}