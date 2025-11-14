package com.toyota.tfs.LpaRedemptionBatch.model.ccp;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class CcpRequestCondition {
    @JsonProperty("attribute")
    private CcpRequestConditionAttribute ccpRequestConditionAttribute;
    @JsonProperty("operator")
    private String operator;
    @JsonProperty("value")
    private CcpRequestConditionValue ccpRequestConditionValue;
}
