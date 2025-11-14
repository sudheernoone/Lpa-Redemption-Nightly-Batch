package com.toyota.tfs.LpaRedemptionBatch.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.toyota.tfs.LpaRedemptionBatch.model.ccp.CcpRequest;
import com.toyota.tfs.LpaRedemptionBatch.model.ccp.CcpResponse;
import com.toyota.tfs.LpaRedemptionBatch.model.dao.LpaMongoDTO;
import com.toyota.tfs.LpaRedemptionBatch.model.dao.RedemptionHistory;
import com.toyota.tfs.LpaRedemptionBatch.model.defi.DefiMongo;
import com.toyota.tfs.LpaRedemptionBatch.model.defi.DefiRequest;
import com.toyota.tfs.LpaRedemptionBatch.model.defi.DefiResponse;
import com.toyota.tfs.LpaRedemptionBatch.model.sparc.SparcBadResponse;
import com.toyota.tfs.LpaRedemptionBatch.model.sparc.SparcMongo;
import com.toyota.tfs.LpaRedemptionBatch.model.sparc.SparcRequest;
import com.toyota.tfs.LpaRedemptionBatch.model.sparc.SparcResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MongoService {

    @Autowired
    MongoTemplate mongoTemplate;

    String mongoId = null;

    public void addTransactionToMongo(RedemptionHistory redemptionHistory) {
        try {
            LpaMongoDTO lpaMongoDTO = new LpaMongoDTO();

            lpaMongoDTO.setAccountNumber(redemptionHistory.getAccountNumber());
            lpaMongoDTO.setTopicMessage(null);
            lpaMongoDTO.setCreatedAt(new Date(System.currentTimeMillis()));
            lpaMongoDTO.setExecutionType("Nightly Batch Processing");

            mongoTemplate.save(lpaMongoDTO);
            mongoId = lpaMongoDTO.getId();

            log.info("addTransactionToMongo - Request has been added in LpaRedemptionAudit MongoDB.Id="+mongoId);

        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("addTransactionToMongo - MongoDB Error:" + ex.getMessage());
        }
    }

    public void updateDefiDataToMongo(DefiRequest defiRequest, DefiResponse defiResponse,
                                      String defiFailedResponse, HttpStatus status) {
        try{
            LpaMongoDTO lpaMongoDTO=mongoTemplate.findById(mongoId,LpaMongoDTO.class);
            DefiMongo defiMongo=new DefiMongo();
            defiMongo.setDefiRequest(defiRequest);
            defiMongo.setDefiResponse(defiResponse);
            defiMongo.setDefiFailedResponse(defiFailedResponse);
            defiMongo.setDefiStatus(status.toString());

            List<DefiMongo> defiMongoList=new ArrayList<>();

            if (lpaMongoDTO != null) {
                if (lpaMongoDTO.getDefiMongoList() !=null) {
                    defiMongoList.addAll(lpaMongoDTO.getDefiMongoList());
                    defiMongoList.add(defiMongo);
                } else {
                    defiMongoList.add(defiMongo);
                }
                lpaMongoDTO.setDefiMongoList(defiMongoList);

                mongoTemplate.save(lpaMongoDTO);
                log.info("updateDefiDataToMongo - defi data has been updated in LpaRedemptionAudit for MongoDB.Id=" + mongoId);

            } else{
                log.info("updateDefiDataToMongo - data could not be retrieved from the MongoDB with the ID="+mongoId);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("updateDefiDataToMongo - MongoDB Error:" + ex.getMessage());
        }
    }

    public void updateCcpDataToMongo(CcpRequest ccpRequest, CcpResponse ccpResponse, String failedResponse, HttpStatus status) {
       try{
            LpaMongoDTO lpaMongoDTO=mongoTemplate.findById(mongoId,LpaMongoDTO.class);

            if (lpaMongoDTO!=null) {
                lpaMongoDTO.setCcpRequest(ccpRequest);
                lpaMongoDTO.setCcpResponse(ccpResponse);
                lpaMongoDTO.setCcpStatus(status.toString());
                lpaMongoDTO.setCcpFailedResponse(failedResponse);

                mongoTemplate.save(lpaMongoDTO);
                log.info("updateCcpDataToMongo - ccp data has been updated in LpaRedemptionAudit for MongoDB.Id=" + mongoId);
            }else{
                log.info("updateCcpDataToMongo - data could not be retrieved from the MongoDB with the ID="+mongoId);
            }
        }catch (Exception ex){
            ex.printStackTrace();
            log.error("updateCcpDataToMongo - MongoDB Error:" + ex.getMessage());
        }
    }

    public void updateSparcDataToMongo(SparcRequest sparcRequest, SparcResponse sparcResponse,
                                       SparcBadResponse sparcBadResponse, String sparcFailedResponse, HttpStatus status) {
        try{
            LpaMongoDTO lpaMongoDTO=mongoTemplate.findById(mongoId,LpaMongoDTO.class);
            SparcMongo sparcMongo = new SparcMongo();
            sparcMongo.setSparcRequest(sparcRequest);
            sparcMongo.setSparcResponse(sparcResponse);
            sparcMongo.setSparcBadResponse(sparcBadResponse);
            sparcMongo.setSparcFailedResponse(sparcFailedResponse);
            sparcMongo.setSparcStatus(status.toString());

            List<SparcMongo> sparcMongoList=new ArrayList<>();

            if (lpaMongoDTO != null) {
                if (lpaMongoDTO.getSparcMongoList() !=null) {
                    sparcMongoList.addAll(lpaMongoDTO.getSparcMongoList());
                    sparcMongoList.add(sparcMongo);
                } else {
                    sparcMongoList.add(sparcMongo);
                }

                lpaMongoDTO.setSparcMongoList(sparcMongoList);

                mongoTemplate.save(lpaMongoDTO);
                log.info("updateSparcDataToMongo - sparc data has been updated in LpaRedemptionAudit for MongoDB.Id=" + mongoId);

            } else{
                log.info("updateSparcDataToMongo - data could not be retrieved from the MongoDB with the ID="+mongoId);
            }

        }catch (Exception ex){
            ex.printStackTrace();
            log.error("updateSparcDataToMongo - MongoDB Error:" + ex.getMessage());
        }
    }

}
