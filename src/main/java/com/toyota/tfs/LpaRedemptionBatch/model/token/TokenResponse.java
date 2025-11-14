package com.toyota.tfs.LpaRedemptionBatch.model.token;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class TokenResponse {

	@JsonProperty
	private List<String> scp;

}
