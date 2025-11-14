package com.toyota.tfs.LpaRedemptionBatch.model.elvis;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsumerCreateResponse {
	
	@JsonProperty("instance_id")
	private String instanceId;
	@JsonProperty("base_uri")
	private String  baseUri;

}
