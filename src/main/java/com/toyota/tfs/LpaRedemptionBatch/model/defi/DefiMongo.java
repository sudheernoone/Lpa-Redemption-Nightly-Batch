package com.toyota.tfs.LpaRedemptionBatch.model.defi;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DefiMongo {
    private DefiRequest defiRequest;
    private DefiResponse defiResponse;
    private String defiFailedResponse;
    private String defiStatus;
}
