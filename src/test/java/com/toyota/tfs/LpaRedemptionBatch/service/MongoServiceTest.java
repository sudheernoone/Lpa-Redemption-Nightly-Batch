package com.toyota.tfs.LpaRedemptionBatch.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;

import com.toyota.tfs.LpaRedemptionBatch.model.ccp.CcpRequest;
import com.toyota.tfs.LpaRedemptionBatch.model.ccp.CcpResponse;
import com.toyota.tfs.LpaRedemptionBatch.model.dao.LpaMongoDTO;
import com.toyota.tfs.LpaRedemptionBatch.model.dao.RedemptionHistory;
import com.toyota.tfs.LpaRedemptionBatch.model.defi.DefiRequest;
import com.toyota.tfs.LpaRedemptionBatch.model.defi.DefiResponse;
import com.toyota.tfs.LpaRedemptionBatch.model.sparc.SparcBadResponse;
import com.toyota.tfs.LpaRedemptionBatch.model.sparc.SparcRequest;
import com.toyota.tfs.LpaRedemptionBatch.model.sparc.SparcResponse;

class MongoServiceTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private MongoService mongoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddTransactionToMongo() {
        // Arrange
        RedemptionHistory redemptionHistory = new RedemptionHistory();
        redemptionHistory.setAccountNumber("12345");

        // Act
        mongoService.addTransactionToMongo(redemptionHistory);

        // Assert
        verify(mongoTemplate, times(1)).save(any(LpaMongoDTO.class));
    }

    @Test
    void testUpdateDefiDataToMongo() {
        // Arrange
        DefiRequest defiRequest = new DefiRequest();
        DefiResponse defiResponse = new DefiResponse();
        String defiFailedResponse = "Failed";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        LpaMongoDTO lpaMongoDTO = new LpaMongoDTO();
        lpaMongoDTO.setAccountNumber("12345");
        mongoService.mongoId="some-mongo-id";
        when(mongoTemplate.findById("some-mongo-id",LpaMongoDTO.class)).thenReturn(lpaMongoDTO);

        // Act
        mongoService.updateDefiDataToMongo(defiRequest, defiResponse, defiFailedResponse, status);

        // Assert
//        verify(mongoTemplate, times(1)).save(any(LpaMongoDTO.class));
    }

    @Test
    void testUpdateCcpDataToMongo() {
        // Arrange
        CcpRequest ccpRequest = new CcpRequest();
        CcpResponse ccpResponse = new CcpResponse();
        String failedResponse = "Failed";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        LpaMongoDTO lpaMongoDTO = new LpaMongoDTO();
        lpaMongoDTO.setAccountNumber("12345");
        mongoService.mongoId="some-mongo-id";

        when(mongoTemplate.findById("some-mongo-id", LpaMongoDTO.class)).thenReturn(lpaMongoDTO);

        // Act
        mongoService.updateCcpDataToMongo(ccpRequest, ccpResponse, failedResponse, status);

        // Assert
//        verify(mongoTemplate, times(1)).save(any(LpaMongoDTO.class));
    }

    @Test
    void testUpdateSparcDataToMongo() {
        // Arrange
        SparcRequest sparcRequest = new SparcRequest();
        SparcResponse sparcResponse = new SparcResponse();
        SparcBadResponse sparcBadResponse = new SparcBadResponse();
        String sparcFailedResponse = "Failed";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        LpaMongoDTO lpaMongoDTO = new LpaMongoDTO();
        lpaMongoDTO.setAccountNumber("12345");
        mongoService.mongoId="some-mongo-id";
        when(mongoTemplate.findById("some-mongo-id", LpaMongoDTO.class)).thenReturn(lpaMongoDTO);

        // Act
        mongoService.updateSparcDataToMongo(sparcRequest, sparcResponse, sparcBadResponse, sparcFailedResponse, status);

        // Assert
//        verify(mongoTemplate, times(1)).save(any(LpaMongoDTO.class));
    }
}