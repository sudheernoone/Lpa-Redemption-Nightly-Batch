package com.toyota.tfs.LpaRedemptionBatch.model.sparc;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class ErrorList {
    @JsonProperty("ErrorCode")
    private String errorCode;
    @JsonProperty("Description")
    private String description;
}
