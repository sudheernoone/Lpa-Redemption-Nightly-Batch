package com.toyota.tfs.LpaRedemptionBatch.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@EnableScheduling
public class LPAJobScheduler {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job lpaJob;
    
    
    @Scheduled(cron = "${cron.time}",zone = "CST") 
    public void LPAJobStarter() {
        Map<String, JobParameter<?>> params = new HashMap<>();
        params.put("currentTime", new JobParameter(System.currentTimeMillis(), LPAJobScheduler.class));

        JobParameters jobParameters = new JobParameters(params);

        try {
            log.info("****  LPA Redemption Job - Start ****");
            JobExecution jobExecution =
                    jobLauncher.run(lpaJob, jobParameters);
            log.info("Job Execution ID = " + jobExecution.getId());
            log.info("****  LPA Redemption Job - End ****");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Exception while starting job");
        }
    }

    @Async
    public void startJob() {
        Map<String, JobParameter<?>> params = new HashMap<>();
        params.put("currentTime", new JobParameter(System.currentTimeMillis(), LPAJobScheduler.class));

        JobParameters jobParameters = new JobParameters(params);

        try {
            log.info("****  LPA Redemption Job - Start ****");
            JobExecution jobExecution =
                    jobLauncher.run(lpaJob, jobParameters);
            log.info("Job Execution ID = " + jobExecution.getId());
            log.info("****  LPA Redemption Job - End ****");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Exception while starting job");
        }
    }
}
