package com.toyota.tfs.LpaRedemptionBatch.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.config.ScheduledTask;

import com.toyota.tfs.LpaRedemptionBatch.config.LPAJobScheduler;
import com.toyota.tfs.LpaRedemptionBatch.util.TokenValidator;

import jakarta.servlet.http.HttpServletRequest;

class SchedulerControllerTest {

    @Mock
    private ScheduledAnnotationBeanPostProcessor postProcessor;

    @Mock
    private LPAJobScheduler schedulerConfiguration;

    @Mock
    private TokenValidator tokenValidator;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private SchedulerController schedulerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        schedulerController = new SchedulerController(request); // Explicitly initialize JobController with the mocked request
        schedulerController.tokenValidator = tokenValidator; // Manually inject the mocked tokenValidator
        schedulerController.schedulerConfiguration = schedulerConfiguration; // Manually inject the mocked lpaJobScheduler
        schedulerController.postProcessor = postProcessor; // Manually inject the mocked jobOperator
    }

    @Test
    void stopSchedule_Success() throws Exception {
        // Arrange
        String token = "valid-token";
        when(request.getHeader("authorization")).thenReturn(token);
        doNothing().when(tokenValidator).validateToken(token);
        doNothing().when(postProcessor).postProcessBeforeDestruction(schedulerConfiguration, "scheduledTasks");

        // Act
        var response = schedulerController.stopSchedule();

        // Assert
        assertNotNull(response);
        assertEquals("JobScheduler Stopped", response.getBody().getStatus());
        assertNull(response.getBody().getError());
        verify(tokenValidator, times(1)).validateToken(token);
        verify(postProcessor, times(1)).postProcessBeforeDestruction(schedulerConfiguration, "scheduledTasks");
    }

    @Test
    void stopSchedule_Failure() throws Exception {
        // Arrange
        String token = "valid-token";
        when(request.getHeader("authorization")).thenReturn(token);
        doNothing().when(tokenValidator).validateToken(token);
        doThrow(new RuntimeException("Error stopping scheduler")).when(postProcessor).postProcessBeforeDestruction(schedulerConfiguration, "scheduledTasks");

        // Act
        var response = schedulerController.stopSchedule();

        // Assert
        assertNotNull(response);
        assertEquals("Error", response.getBody().getStatus());
        assertNotNull(response.getBody().getError());
        assertEquals("JobScheduler couldn't be stopped, check logs", response.getBody().getError().getMessage());
        verify(tokenValidator, times(1)).validateToken(token);
        verify(postProcessor, times(1)).postProcessBeforeDestruction(schedulerConfiguration, "scheduledTasks");
    }

    @Test
    void startSchedule_Success() throws Exception {
        // Arrange
        String token = "valid-token";
        when(request.getHeader("authorization")).thenReturn(token);
        doNothing().when(tokenValidator).validateToken(token);
        // Act
        var response = schedulerController.startSchedule();

        // Assert
        assertNotNull(response);
        assertEquals("JobScheduler Started", response.getBody().getStatus());
        assertNull(response.getBody().getError());
        verify(tokenValidator, times(1)).validateToken(token);
        verify(postProcessor, times(1)).postProcessAfterInitialization(schedulerConfiguration, "scheduledTasks");
    }

    @Test
    void startSchedule_Failure() throws Exception {
        // Arrange
        String token = "valid-token";
        when(request.getHeader("authorization")).thenReturn(token);
        doNothing().when(tokenValidator).validateToken(token);
        doThrow(new RuntimeException("Error starting scheduler")).when(postProcessor).postProcessAfterInitialization(schedulerConfiguration, "scheduledTasks");

        // Act
        var response = schedulerController.startSchedule();

        // Assert
        assertNotNull(response);
        assertEquals("Error", response.getBody().getStatus());
        assertNotNull(response.getBody().getError());
        assertEquals("JobScheduler couldn't be started, check logs", response.getBody().getError().getMessage());
        verify(tokenValidator, times(1)).validateToken(token);
        verify(postProcessor, times(1)).postProcessAfterInitialization(schedulerConfiguration, "scheduledTasks");
    }

    @Test
    void listSchedules_Success() throws Exception {
        // Arrange
        String token = "valid-token";
        when(request.getHeader("authorization")).thenReturn(token);
        doNothing().when(tokenValidator).validateToken(token);
        Set<ScheduledTask> tasks = Collections.singleton(mock(ScheduledTask.class));
        when(postProcessor.getScheduledTasks()).thenReturn(tasks);

        // Act
        var response = schedulerController.listSchedules();

        // Assert
        assertNotNull(response);
        assertEquals(tasks.toString(), response.getBody().getStatus());
        assertNull(response.getBody().getError());
        verify(tokenValidator, times(1)).validateToken(token);
        verify(postProcessor, times(1)).getScheduledTasks();
    }

    @Test
    void listSchedules_Empty() throws Exception {
        // Arrange
        String token = "valid-token";
        when(request.getHeader("authorization")).thenReturn(token);
        doNothing().when(tokenValidator).validateToken(token);
        when(postProcessor.getScheduledTasks()).thenReturn(Collections.emptySet());

        // Act
        var response = schedulerController.listSchedules();

        // Assert
        assertNotNull(response);
        assertEquals("Currently no scheduler tasks are running", response.getBody().getStatus());
        assertNull(response.getBody().getError());
        verify(tokenValidator, times(1)).validateToken(token);
        verify(postProcessor, times(1)).getScheduledTasks();
    }

    @Test
    void listSchedules_Failure() throws Exception {
        // Arrange
        String token = "valid-token";
        when(request.getHeader("authorization")).thenReturn(token);
        doNothing().when(tokenValidator).validateToken(token);
        doThrow(new RuntimeException("Job start error")).when(postProcessor).getScheduledTasks();

        // Act
        var response = schedulerController.listSchedules();

        // Assert
        assertNotNull(response);
        assertEquals("Error", response.getBody().getStatus());
        verify(tokenValidator, times(1)).validateToken(token);
        verify(postProcessor, times(1)).getScheduledTasks();
    }
}