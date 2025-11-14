package com.toyota.tfs.LpaRedemptionBatch.model.sparc;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class VehicleInfo {
    @JsonProperty("AccessoryCode")
    private List<String> AccessoryCode;
    @JsonProperty("AgeOfVehicle")
    private long AgeOfVehicle;
    @JsonProperty("ExteriorColor")
    private String ExteriorColor;
    @JsonProperty("InteriorColor")
    private String InteriorColor;
    @JsonProperty("Make")
    private String Make;
    @JsonProperty("ManufacturerType")
    private String ManufacturerType;
    @JsonProperty("ModelCode")
    private String ModelCode;
    @JsonProperty("ModelName")
    private String ModelName;
    @JsonProperty("ModelYear")
    private String ModelYear;
    @JsonProperty("PhaseCode")
    private String PhaseCode;
    @JsonProperty("VIN")
    private String VIN;
    @JsonProperty("VehicleCondition")
    private String VehicleCondition;
}
