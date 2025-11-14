package com.toyota.tfs.LpaRedemptionBatch.model.sparc;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Warning {
	@JsonProperty("Description")
	private String description;
	@JsonProperty("WarningCode")
	private String warningCode;
	@JsonProperty("WarningResourceKey")
	private String warningResourceKey;
}
