package com.toyota.tfs.LpaRedemptionBatch.model.sparc;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Error {
    @JsonProperty("Description")
    private String description;
    @JsonProperty("ErrorCode")
    private String errorCode;
    @JsonProperty("ErrorResourceKey")
    private String errorResourceKey;
    @JsonProperty("EventLogId")
    private String eventLogId;
    @JsonProperty("ExceptionMessage")
    private String exceptionMessage;
    @JsonProperty("IsBusinessError")
    private String isBusinessError;
    @JsonProperty("StackTrace")
    private String stackTrace;
}
