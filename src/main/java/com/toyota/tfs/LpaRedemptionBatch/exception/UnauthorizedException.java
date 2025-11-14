package com.toyota.tfs.LpaRedemptionBatch.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends Exception{

    String errorCode;
    String errorMessage;

    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode=errorCode;
        this.errorMessage=errorMessage;
    }

    public UnauthorizedException(final String message) {
        super(message);
    }

    public UnauthorizedException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
