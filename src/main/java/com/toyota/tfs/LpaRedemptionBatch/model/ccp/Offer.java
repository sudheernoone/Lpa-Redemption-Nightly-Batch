package com.toyota.tfs.LpaRedemptionBatch.model.ccp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Offer {
   
    private String offerStartDate;
    private String offerEndDate;
    private String offerName;
    private String programName;
}
