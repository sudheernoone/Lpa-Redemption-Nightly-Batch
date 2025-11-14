package com.toyota.tfs.LpaRedemptionBatch.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class ServiceParameterExceptionTest {

    @Test
    void testDefaultConstructor() {
        ServiceParameterException exception = new ServiceParameterException();
        assertNull(exception.getMessage());
        assertNull(exception.getErrorCode());
        assertNull(exception.getErrorMessage());
    }

    @Test
    void testConstructorWithErrorCodeAndMessage() {
        String errorCode = "ERR001";
        String errorMessage = "Invalid parameter";
        ServiceParameterException exception = new ServiceParameterException(errorCode, errorMessage);

        assertEquals(errorCode, exception.getErrorCode());
        assertEquals(errorMessage, exception.getErrorMessage());
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testConstructorWithMessage() {
        String message = "An error occurred";
        ServiceParameterException exception = new ServiceParameterException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getErrorCode());
        assertNull(exception.getErrorMessage());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        String message = "An error occurred";
        Throwable cause = new RuntimeException("Cause of the error");
        ServiceParameterException exception = new ServiceParameterException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertNull(exception.getErrorCode());
        assertNull(exception.getErrorMessage());
    }
}