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
public class ConsumerCreateInstance {
	
	@JsonProperty("name")
	private String name;
	@JsonProperty("format")
	private String format;
	@JsonProperty("auto.offset.reset")
	private String reset;

}
