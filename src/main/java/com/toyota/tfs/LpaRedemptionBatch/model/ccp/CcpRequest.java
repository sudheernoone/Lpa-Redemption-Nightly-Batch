package com.toyota.tfs.LpaRedemptionBatch.model.ccp;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class CcpRequest {
    @JsonProperty("request")
    private CcpRequestContext ccpRequestContext;
    @JsonProperty("conditionSet")
    private CcpRequestConditionSet ccpRequestConditionSet;
}
