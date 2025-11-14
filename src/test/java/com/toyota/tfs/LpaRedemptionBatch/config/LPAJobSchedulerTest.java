package com.toyota.tfs.LpaRedemptionBatch.config;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;

class LPAJobSchedulerTest {

    @Mock
    private JobLauncher jobLauncher;

    @Mock
    private Job lpaJob;

    private LPAJobScheduler lpaJobScheduler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        lpaJobScheduler = new LPAJobScheduler();
        lpaJobScheduler.jobLauncher=jobLauncher;
        lpaJobScheduler.lpaJob=lpaJob;
    }

    @Test
    void startJob_Success() throws Exception {
        // Arrange
        JobExecution jobExecution = mock(JobExecution.class);
        when(jobLauncher.run(any(Job.class), any(JobParameters.class))).thenReturn(jobExecution);
        when(jobExecution.getId()).thenReturn(1L);

        // Act & Assert
        assertDoesNotThrow(() -> lpaJobScheduler.startJob());
        verify(jobLauncher, times(1)).run(eq(lpaJob), any(JobParameters.class));
    }

    @Test
    void startJob_Exception() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Job failed")).when(jobLauncher).run(any(Job.class), any(JobParameters.class));

        // Act & Assert
        assertDoesNotThrow(() -> lpaJobScheduler.startJob());
        verify(jobLauncher, times(1)).run(eq(lpaJob), any(JobParameters.class));
    }
    @Test
    void LPAJobStarter_Success() throws Exception {
        // Arrange
        JobExecution jobExecution = mock(JobExecution.class);
        when(jobLauncher.run(any(Job.class), any(JobParameters.class))).thenReturn(jobExecution);
        when(jobExecution.getId()).thenReturn(1L);

        // Act & Assert
        assertDoesNotThrow(() -> lpaJobScheduler.LPAJobStarter());

    }
    @Test
    void LPAJobStarter_Failure() throws Exception {
        // Arrange
        JobExecution jobExecution = mock(JobExecution.class);
        when(jobLauncher.run(any(Job.class), any(JobParameters.class))).thenThrow(new RuntimeException("Job failed"));
        when(jobExecution.getId()).thenReturn(1L);

        // Act & Assert
        assertDoesNotThrow(() -> lpaJobScheduler.LPAJobStarter());

    }
}