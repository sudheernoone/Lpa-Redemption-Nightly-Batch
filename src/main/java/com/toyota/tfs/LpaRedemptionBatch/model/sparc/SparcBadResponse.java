package com.toyota.tfs.LpaRedemptionBatch.model.sparc;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class SparcBadResponse {
    @JsonProperty("CorrelationId")
    private String correlationId;
    @JsonProperty("Errors")
    private List<ErrorList> errors;
}
