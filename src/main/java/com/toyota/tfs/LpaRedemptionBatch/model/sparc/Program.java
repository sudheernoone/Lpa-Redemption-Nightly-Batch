package com.toyota.tfs.LpaRedemptionBatch.model.sparc;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Program {
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("EffectiveDate")
    private String effectiveDate;
    @JsonProperty("EndDate")
    private String endDate;
    @JsonProperty("OptionId")
    private String optionId;
    @JsonProperty("OptionTypeId")
    private String optionTypeId;
    @JsonProperty("OptionDetailId")
    private String optionDetailId;
    @JsonProperty("SourceId")
    private String sourceId;
    @JsonProperty("CostShare")
    private String costShare;
    @JsonProperty("ContractRequired")
    private String contractRequired;
    @JsonProperty("ProgramType")
    private String programType;
    
    @JsonProperty("Value")
    private String value;
    @JsonProperty("WaivableMonthlyPayments")
    private String waivableMonthlyPayments;
    @JsonProperty("Disclaimer")
    private String disclaimer;
    
    @JsonProperty("ProgramExclusions")
    private List<String> programExclusions;
    
    @JsonProperty("EligibleNewVehicles")
    private List<VehicleInfo> eligibleNewVehicles;
    
    @JsonProperty("DealerInfo")
    private DealerInfo dealerInfo;
    
}
