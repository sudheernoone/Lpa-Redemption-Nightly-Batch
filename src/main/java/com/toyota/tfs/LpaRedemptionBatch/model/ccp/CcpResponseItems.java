package com.toyota.tfs.LpaRedemptionBatch.model.ccp;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class CcpResponseItems {
    @JsonProperty("values")
    private List<CcpResponseValues> ccpResponseValues;

}
