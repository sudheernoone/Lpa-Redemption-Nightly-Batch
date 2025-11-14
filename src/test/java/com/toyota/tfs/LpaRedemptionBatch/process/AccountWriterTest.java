package com.toyota.tfs.LpaRedemptionBatch.process;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.Chunk;

import com.toyota.tfs.LpaRedemptionBatch.model.dao.RedemptionHistory;

class AccountWriterTest {

    private AccountWriter accountWriter;

    @BeforeEach
    void setUp() {
        accountWriter = new AccountWriter();
    }

    @Test
    void testWrite() throws Exception {
        // Arrange
        RedemptionHistory history1 = new RedemptionHistory();
        history1.setAccountNumber("12345");

        RedemptionHistory history2 = new RedemptionHistory();
        history2.setAccountNumber("67890");

        Chunk<RedemptionHistory> chunk = new Chunk<>(Arrays.asList(history1, history2));

        // Act
        accountWriter.write(chunk);

        // Assert
        // Verify logs or behavior if needed (mocking loggers or other dependencies)
    }
}