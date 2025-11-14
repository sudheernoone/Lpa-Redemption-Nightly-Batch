package com.toyota.tfs.LpaRedemptionBatch.controller;

import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toyota.tfs.LpaRedemptionBatch.config.LPAJobScheduler;
import com.toyota.tfs.LpaRedemptionBatch.model.token.TokenResponseError;
import com.toyota.tfs.LpaRedemptionBatch.model.token.TokenResponseMessage;
import com.toyota.tfs.LpaRedemptionBatch.util.TokenValidator;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/lpa-redemption-nightly-batch-job/api/job")
public class JobController {
	
	@Autowired
	LPAJobScheduler lpaJobScheduler;
	@Autowired
	JobOperator jobOperator;
	@Autowired
	TokenValidator tokenValidator;

	private HttpServletRequest request;

	@Autowired
	public JobController(HttpServletRequest request) {
		this.request = request;
	}

	@GetMapping("/start")
	public ResponseEntity<TokenResponseMessage> startJob() throws Exception {
		TokenResponseMessage message = new TokenResponseMessage();
		String token = request.getHeader("authorization");
		tokenValidator.validateToken(token);
		try {
			lpaJobScheduler.startJob();
			message.setStatus("Job Started");
			message.setError(null);
		} catch (Exception e){
			e.printStackTrace();
			TokenResponseError tokenResponseError=new TokenResponseError();
			tokenResponseError.setCode(null);
			tokenResponseError.setMessage("job couldn't be started, check logs");
			message.setStatus("Error");
			message.setError(tokenResponseError);
		}
		return ResponseEntity.ok(message);
	}
	
	@GetMapping("/stop/{jobExecutionId}")
	public ResponseEntity<TokenResponseMessage> stopJob(@PathVariable long jobExecutionId) throws Exception {
		TokenResponseMessage message=new TokenResponseMessage();
		String token = request.getHeader("authorization");
		tokenValidator.validateToken(token);
		try {
			jobOperator.stop(jobExecutionId);
			message.setStatus("Job Stopped");
			message.setError(null);
		} catch (Exception e) {
			e.printStackTrace();
			TokenResponseError tokenResponseError=new TokenResponseError();
			tokenResponseError.setCode(null);
			tokenResponseError.setMessage("job couldn't be stopped, check logs");
			message.setStatus("Error");
			message.setError(tokenResponseError);
		}
		return ResponseEntity.ok(message);
	}
}
