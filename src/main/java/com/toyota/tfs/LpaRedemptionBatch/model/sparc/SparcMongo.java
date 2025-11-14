package com.toyota.tfs.LpaRedemptionBatch.model.sparc;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class SparcMongo {
    private SparcRequest sparcRequest;
    private SparcResponse sparcResponse;
    private SparcBadResponse sparcBadResponse;
    private String sparcFailedResponse;
    private String sparcStatus;
}
