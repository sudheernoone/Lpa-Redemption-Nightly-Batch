package com.toyota.tfs.LpaRedemptionBatch.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.toyota.tfs.LpaRedemptionBatch.config.LPAJobScheduler;
import com.toyota.tfs.LpaRedemptionBatch.model.token.TokenResponseError;
import com.toyota.tfs.LpaRedemptionBatch.model.token.TokenResponseMessage;
import com.toyota.tfs.LpaRedemptionBatch.util.TokenValidator;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/lpa-redemption-nightly-batch-job/scheduler")
public class SchedulerController {
    private static final String SCHEDULED_TASKS = "scheduledTasks";
    @Autowired
    ScheduledAnnotationBeanPostProcessor postProcessor;
    @Autowired
    LPAJobScheduler schedulerConfiguration;
    @Autowired
    TokenValidator tokenValidator;
    private HttpServletRequest request;

    @Autowired
    public SchedulerController(HttpServletRequest request) {
        this.request = request;
    }
    @GetMapping(value = "/stop")
    public ResponseEntity<TokenResponseMessage> stopSchedule() throws Exception {
        TokenResponseMessage message = new TokenResponseMessage();
        String token = request.getHeader("authorization");
        tokenValidator.validateToken(token);
        try {
            postProcessor.postProcessBeforeDestruction(schedulerConfiguration, SCHEDULED_TASKS);
            message.setStatus("JobScheduler Stopped");
            message.setError(null);
        } catch(Exception e){
            e.printStackTrace();
            TokenResponseError tokenResponseError=new TokenResponseError();
            tokenResponseError.setCode(null);
            tokenResponseError.setMessage("JobScheduler couldn't be stopped, check logs");
            message.setStatus("Error");
            message.setError(tokenResponseError);
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping(value = "/start")
    public ResponseEntity<TokenResponseMessage> startSchedule() throws Exception {
        TokenResponseMessage message = new TokenResponseMessage();
        String token = request.getHeader("authorization");
        tokenValidator.validateToken(token);
        try {
            postProcessor.postProcessAfterInitialization(schedulerConfiguration, SCHEDULED_TASKS);
            message.setStatus("JobScheduler Started");
            message.setError(null);
        } catch(Exception e){
            e.printStackTrace();
            TokenResponseError tokenResponseError=new TokenResponseError();
            tokenResponseError.setCode(null);
            tokenResponseError.setMessage("JobScheduler couldn't be started, check logs");
            message.setStatus("Error");
            message.setError(tokenResponseError);
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping(value = "/list")
    public ResponseEntity<TokenResponseMessage> listSchedules() throws JsonProcessingException,Exception {
        TokenResponseMessage message = new TokenResponseMessage();
        String token = request.getHeader("authorization");
        tokenValidator.validateToken(token);
        try {
            Set<ScheduledTask> setTasks = postProcessor.getScheduledTasks();
            if (!setTasks.isEmpty()) {
                message.setStatus(setTasks.toString());
            } else {
                message.setStatus("Currently no scheduler tasks are running");
            }
            message.setError(null);
        }catch(Exception e){
            e.printStackTrace();
            TokenResponseError tokenResponseError=new TokenResponseError();
            tokenResponseError.setCode(null);
            tokenResponseError.setMessage("JobScheduler list not retrieved, check logs");
            message.setStatus("Error");
            message.setError(tokenResponseError);
        }
        return ResponseEntity.ok(message);
    }
}
