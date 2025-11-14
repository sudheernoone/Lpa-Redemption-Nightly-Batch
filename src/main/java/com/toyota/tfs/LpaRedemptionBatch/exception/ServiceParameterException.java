package com.toyota.tfs.LpaRedemptionBatch.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ServiceParameterException extends Exception{

    String errorCode;
    String errorMessage;

    public ServiceParameterException() {
        super();
    }

    public ServiceParameterException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode=errorCode;
        this.errorMessage=errorMessage;
    }

    public ServiceParameterException(final String message) {
        super(message);
    }

    public ServiceParameterException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
