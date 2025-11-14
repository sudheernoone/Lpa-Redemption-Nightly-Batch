package com.toyota.tfs.LpaRedemptionBatch.model.sparc;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
@Getter
@Setter
public class LpaQualifiers {
    @JsonProperty("LpaQualifierSequenceNumber")
    private long LpaQualifierSequenceNumber;
    @JsonProperty("Product")
    private String Product;
    @JsonProperty("FinanceProgramType")
    private String FinanceProgramType;
    @JsonProperty("NewVehicleFlag")
    private boolean NewVehicleFlag;
    @JsonProperty("ContractDate")
    private String ContractDate;
    @JsonProperty("MaturityDate")
    private String MaturityDate;
    @JsonProperty("ReturnDate")
    private String ReturnDate;
    @JsonProperty("GroundingDate")
    private String GroundingDate;
    @JsonProperty("NumberOfPaymentsRemaining")
    private long NumberOfPaymentsRemaining;
    @JsonProperty("Tier")
    private String Tier;
    @JsonProperty("Term")
    private String Term;
    @JsonProperty("GroupAreaId")
    private long GroupAreaId;
    
    @JsonProperty("NewVehicleInfo")
    private VehicleInfo NewVehicleInfo;
    @JsonProperty("ReturningVehicleInfo")
    private VehicleInfo ReturningVehicleInfo;
    @JsonProperty("DealerInfo")
    private DealerInfo DealerInfo;
    
    @JsonProperty("Programs")
    private List<Program> Programs;
    @JsonProperty("Warnings")
    private List<Warning> Warnings;
    @JsonProperty("Errors")
    private List<Error> Errors;
}
