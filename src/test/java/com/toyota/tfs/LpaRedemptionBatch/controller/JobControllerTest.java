package com.toyota.tfs.LpaRedemptionBatch.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.http.ResponseEntity;

import com.toyota.tfs.LpaRedemptionBatch.config.LPAJobScheduler;
import com.toyota.tfs.LpaRedemptionBatch.model.token.TokenResponseMessage;
import com.toyota.tfs.LpaRedemptionBatch.util.TokenValidator;

import jakarta.servlet.http.HttpServletRequest;

class JobControllerTest {

    @Mock
    private LPAJobScheduler lpaJobScheduler;

    @Mock
    private JobOperator jobOperator;

    @Mock
    private TokenValidator tokenValidator;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private JobController jobController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        jobController = new JobController(request); // Explicitly initialize JobController with the mocked request
        jobController.tokenValidator = tokenValidator; // Manually inject the mocked tokenValidator
        jobController.lpaJobScheduler = lpaJobScheduler; // Manually inject the mocked lpaJobScheduler
        jobController.jobOperator = jobOperator; // Manually inject the mocked jobOperator
    }

    @Test
    void startJob_Success() throws Exception {
        // Arrange
        String token = "valid-token";
        when(request.getHeader("authorization")).thenReturn(token);
        doNothing().when(tokenValidator).validateToken(token);
        doNothing().when(lpaJobScheduler).startJob();

        // Act
        ResponseEntity<TokenResponseMessage> response = jobController.startJob();

        // Assert
        assertNotNull(response);
        assertEquals("Job Started", response.getBody().getStatus());
        assertNull(response.getBody().getError());
        verify(tokenValidator, times(1)).validateToken(token);
        verify(lpaJobScheduler, times(1)).startJob();
    }

    @Test
    void startJob_Failure() throws Exception {
        // Arrange
        String token = "valid-token";
        when(request.getHeader("authorization")).thenReturn(token);
        doNothing().when(tokenValidator).validateToken(token);
        doThrow(new RuntimeException("Job start error")).when(lpaJobScheduler).startJob();

        // Act
        ResponseEntity<TokenResponseMessage> response = jobController.startJob();

        // Assert
        assertNotNull(response);
        assertEquals("Error", response.getBody().getStatus());
        assertNotNull(response.getBody().getError());
        assertEquals("job couldn't be started, check logs", response.getBody().getError().getMessage());
        verify(tokenValidator, times(1)).validateToken(token);
        verify(lpaJobScheduler, times(1)).startJob();
    }

    @Test
    void stopJob_Success() throws Exception {
        // Arrange
        long jobExecutionId = 123L;
        String token = "valid-token";
        when(request.getHeader("authorization")).thenReturn(token);
        doNothing().when(tokenValidator).validateToken(token);
        when(jobOperator.stop(jobExecutionId)).thenReturn(true); // Adjust based on the actual return type of stop()

        // Act
        ResponseEntity<TokenResponseMessage> response = jobController.stopJob(jobExecutionId);

        // Assert
        assertNotNull(response);
        assertEquals("Job Stopped", response.getBody().getStatus());
        assertNull(response.getBody().getError());
        verify(tokenValidator, times(1)).validateToken(token);
        verify(jobOperator, times(1)).stop(jobExecutionId);
    }

    @Test
    void stopJob_Failure() throws Exception {
        // Arrange
        long jobExecutionId = 123L;
        String token = "valid-token";
        when(request.getHeader("authorization")).thenReturn(token);
        doNothing().when(tokenValidator).validateToken(token);
        doThrow(new RuntimeException("Job stop error")).when(jobOperator).stop(jobExecutionId);

        // Act
        ResponseEntity<TokenResponseMessage> response = jobController.stopJob(jobExecutionId);

        // Assert
        assertNotNull(response);
        assertEquals("Error", response.getBody().getStatus());
        assertNotNull(response.getBody().getError());
        assertEquals("job couldn't be stopped, check logs", response.getBody().getError().getMessage());
        verify(tokenValidator, times(1)).validateToken(token);
        verify(jobOperator, times(1)).stop(jobExecutionId);
    }
}