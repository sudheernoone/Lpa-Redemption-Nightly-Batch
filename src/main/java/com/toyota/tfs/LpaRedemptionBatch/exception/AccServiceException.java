package com.toyota.tfs.LpaRedemptionBatch.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;
import lombok.Setter;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@Getter
@Setter
public class AccServiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private String errCode;
	
	private String errMsg;

	public AccServiceException() {
        super();
    }

    public AccServiceException(final String message) {
        super(message);
        this.errMsg = message;
    }

    public AccServiceException(final String errorCode, final String errMsg) {
        super(errMsg);
        this.errMsg = errMsg;
        this.errCode = errorCode;
    }
    
}