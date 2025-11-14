/*package com.toyota.tfs.LpaRedemptionBatch.config;

import com.toyota.tfs.LpaRedemptionBatch.process.AccountProcessor;
import com.toyota.tfs.LpaRedemptionBatch.process.AccountReader;
import com.toyota.tfs.LpaRedemptionBatch.process.AccountWriter;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class BatchConfigTest {

    @InjectMocks
    private BatchConfig batchConfig;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private PlatformTransactionManager transactionManager;

    @Mock
    private AccountReader accountReader;

    @Mock
    private AccountProcessor accountProcessor;

    @Mock
    private AccountWriter accountWriter;

    @Test
    void lpaRedemptionNightlyBatchJob_ShouldReturnValidJob() throws Exception {
        // Act
        Job job = batchConfig.lpaRedemptionNightlyBatchJob(jobRepository, batchConfig.lpaRedemptionStep(jobRepository, transactionManager));

        // Assert
        assertNotNull(job, "lpaRedemptionNightlyBatchJob should not be null");
    }

    @Test
    void lpaRedemptionStep_ShouldReturnValidStep() throws Exception {
        // Act
        Step step = batchConfig.lpaRedemptionStep(jobRepository, transactionManager);

        // Assert
        assertNotNull(step, "lpaRedemptionStep should not be null");
    }
}*/