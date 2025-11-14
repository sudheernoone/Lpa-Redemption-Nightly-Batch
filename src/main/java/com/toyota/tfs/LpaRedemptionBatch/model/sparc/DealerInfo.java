package com.toyota.tfs.LpaRedemptionBatch.model.sparc;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class DealerInfo {
    @JsonProperty("DealerId")
    private String DealerId;
    @JsonProperty("DealerState")
    private String DealerState;
    @JsonProperty("DealerType")
    private String DealerType;
    @JsonProperty("DealerZip")
    private String DealerZip;
    @JsonProperty("CenterNumber")
    private String CenterNumber;
    @JsonProperty("ManufacturerType")
    private String ManufacturerType;
}
