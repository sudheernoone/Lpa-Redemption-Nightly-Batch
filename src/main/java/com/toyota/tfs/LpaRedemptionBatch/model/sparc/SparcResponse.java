package com.toyota.tfs.LpaRedemptionBatch.model.sparc;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class SparcResponse {
	@JsonProperty("SenderId")
    private String SenderId;
    @JsonProperty("ClientId")
    private String ClientId;
    @JsonProperty("TenantId")
    private String TenantId;
    @JsonProperty("RequestType")
    private String RequestType;
    @JsonProperty("LpaQualifiers")
    private List<LpaQualifiers> LpaQualifiers;
}
