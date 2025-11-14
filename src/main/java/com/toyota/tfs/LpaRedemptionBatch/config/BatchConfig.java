package com.toyota.tfs.LpaRedemptionBatch.config;

import java.text.ParseException;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.toyota.tfs.LpaRedemptionBatch.model.dao.RedemptionHistory;
import com.toyota.tfs.LpaRedemptionBatch.process.AccountProcessor;
import com.toyota.tfs.LpaRedemptionBatch.process.AccountReader;
import com.toyota.tfs.LpaRedemptionBatch.process.AccountWriter;

@Configuration
public class BatchConfig {

    @Autowired
    AccountReader accountReader;
    @Autowired
    AccountProcessor accountProcessor;
    @Autowired
    AccountWriter accountWriter;

    @Bean
    public Job lpaRedemptionNightlyBatchJob(JobRepository jobRepository,Step lpaRedemptionStep){
        return new JobBuilder("lpaRedemptionNightlyBatchJob",jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(lpaRedemptionStep)
                .end()
                .build();
    }

    @Bean
    public Step lpaRedemptionStep(JobRepository jobRepository,PlatformTransactionManager transactionManager) throws UnexpectedInputException, ParseException, NonTransientResourceException, Exception  {
        return new StepBuilder("lpaRedemptionStep",jobRepository)
                .<RedemptionHistory, RedemptionHistory> chunk(5,transactionManager)
                .reader(accountReader.jdbcCursorItemReader())
                .processor(accountProcessor)
                .writer(accountWriter)
                .build();
    }

}
