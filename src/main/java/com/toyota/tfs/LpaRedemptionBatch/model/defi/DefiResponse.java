package com.toyota.tfs.LpaRedemptionBatch.model.defi;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class DefiResponse{

	@JsonProperty
	private String ActivityId;
	@JsonProperty
	private String AccountNumber;
	@JsonProperty
	private String WorkRequestInstanceId;
	@JsonProperty
	private List<Error> Errors;
	@JsonProperty
	private boolean HasErrors;
}
