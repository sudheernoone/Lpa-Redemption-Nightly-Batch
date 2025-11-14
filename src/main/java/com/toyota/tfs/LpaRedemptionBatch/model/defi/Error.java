package com.toyota.tfs.LpaRedemptionBatch.model.defi;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Error {
	
	@JsonProperty
	private String ErrorCode;
	@JsonProperty
	private String Description;
	@JsonProperty
	private boolean IsBusinessError;
	@JsonProperty
	private String EventLogId;

}
