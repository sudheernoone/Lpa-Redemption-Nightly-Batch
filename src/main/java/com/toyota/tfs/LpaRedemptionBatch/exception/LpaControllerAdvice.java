package com.toyota.tfs.LpaRedemptionBatch.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.toyota.tfs.LpaRedemptionBatch.model.token.TokenResponseError;
import com.toyota.tfs.LpaRedemptionBatch.model.token.TokenResponseMessage;
import com.toyota.tfs.LpaRedemptionBatch.util.ErrorMessageHandler;
import com.toyota.tfs.LpaRedemptionBatch.util.ErrorMessageHandler.MESSAGE_CODE;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class LpaControllerAdvice {
    @Autowired
    ErrorMessageHandler errorMessageHandler;

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> processUnauthorizedException(UnauthorizedException ex, WebRequest request) {
        log.error("#ServiceError - UnauthorizedException: "+ ex);

        TokenResponseError tokenResponseError =new TokenResponseError();
        tokenResponseError.setCode(ex.getErrorCode());
        tokenResponseError.setMessage(ex.getErrorMessage());

        TokenResponseMessage resp = new TokenResponseMessage(MESSAGE_CODE.ERROR_STATUS, tokenResponseError);

        return new ResponseEntity<>(resp, HttpStatus.UNAUTHORIZED);
    }
}
