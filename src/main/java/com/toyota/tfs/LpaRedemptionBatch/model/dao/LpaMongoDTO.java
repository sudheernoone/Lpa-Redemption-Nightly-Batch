package com.toyota.tfs.LpaRedemptionBatch.model.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.toyota.tfs.LpaRedemptionBatch.model.ccp.CcpRequest;
import com.toyota.tfs.LpaRedemptionBatch.model.ccp.CcpResponse;
import com.toyota.tfs.LpaRedemptionBatch.model.defi.DefiMongo;
import com.toyota.tfs.LpaRedemptionBatch.model.elvis.TopicMessage;
import com.toyota.tfs.LpaRedemptionBatch.model.sparc.SparcMongo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "LpaRedemptionAudit")
public class LpaMongoDTO {

    @Id
    private String id;

    /* Request Fields - updated after receiving grounding request */
    private String accountNumber;
    private TopicMessage topicMessage;
    private Date createdAt;
    private String executionType;

    /* Fields to be updated after successful execution or whenever there is an error*/
    private CcpRequest ccpRequest;
    private CcpResponse ccpResponse;
    private String ccpFailedResponse;
    private String ccpStatus;
    private List<SparcMongo> sparcMongoList;
    private List<DefiMongo> defiMongoList;
}
