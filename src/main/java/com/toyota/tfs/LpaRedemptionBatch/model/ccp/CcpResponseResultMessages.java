package com.toyota.tfs.LpaRedemptionBatch.model.ccp;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class CcpResponseResultMessages {
    @JsonProperty("resultType")
    private String resultType;
    @JsonProperty("resultClass")
    private String resultClass;
    @JsonProperty("resultCode")
    private String resultCode;
    @JsonProperty("message")
    private String message;
}
