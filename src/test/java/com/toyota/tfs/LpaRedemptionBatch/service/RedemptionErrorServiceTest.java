package com.toyota.tfs.LpaRedemptionBatch.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.toyota.tfs.LpaRedemptionBatch.model.dao.RedemptionError;
import com.toyota.tfs.LpaRedemptionBatch.repository.RedemptionErrorRepository;

@ExtendWith(MockitoExtension.class)
class RedemptionErrorServiceTest {

    @Mock
    private RedemptionErrorRepository redemptionErrorRepository;

    @InjectMocks
    private RedemptionErrorService redemptionErrorService;

    @Test
    void testAddErrorToTable() {
        // Arrange
        String accountNumber = "12345";
        String tenantId = "tenant1";
        String errorCode = "ERR001";
        String errorMsg = "Test error message";

        // Act
        redemptionErrorService.addErrorToTable(accountNumber, tenantId, errorCode, errorMsg);

        // Assert
        ArgumentCaptor<RedemptionError> captor = ArgumentCaptor.forClass(RedemptionError.class);
        verify(redemptionErrorRepository, times(1)).save(captor.capture());

        RedemptionError capturedError = captor.getValue();
        assertNotNull(capturedError);
        assertEquals(accountNumber, capturedError.getAccountNumber());
        assertEquals(tenantId, capturedError.getTenantId());
        assertEquals(errorCode, capturedError.getErrorCode());
        assertEquals(errorMsg, capturedError.getErrorMessage());
        assertEquals("N", capturedError.getMailSent());
        assertNotNull(capturedError.getRecordInsertDate());
        assertTrue(LocalDateTime.parse(capturedError.getRecordInsertDate()).isBefore(LocalDateTime.now()));
    }
}