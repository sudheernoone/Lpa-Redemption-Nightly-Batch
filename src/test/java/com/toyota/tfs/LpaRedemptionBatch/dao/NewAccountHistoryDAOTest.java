package com.toyota.tfs.LpaRedemptionBatch.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.toyota.tfs.LpaRedemptionBatch.model.dao.NewAccountHistory;
import com.toyota.tfs.LpaRedemptionBatch.repository.NewAccountRepository;

@ExtendWith(MockitoExtension.class)
class NewAccountHistoryDAOTest {

    @Mock
    private NewAccountRepository newAccountRepository;

    @InjectMocks
    private NewAccountHistoryDAO newAccountHistoryDAO;

    @BeforeEach
    void setUp() {
        // Initialize mocks
    }

    @Test
    void testGetNewAccountByGroundedAccountNumerAndNewAccountNumber() {
        // Arrange
        String groundedAccountNumber = "grounded123";
        String newAccountNumber = "new123";
        String tenantId = "tenant1";

        NewAccountHistory mockAccount = new NewAccountHistory();
        mockAccount.setGroundedAccountNumber(groundedAccountNumber);
        mockAccount.setNewAccountNumber(newAccountNumber);

        when(newAccountRepository.getNewAccountByGroundedAccountNumerAndNewAccountNumber(groundedAccountNumber, newAccountNumber, tenantId))
                .thenReturn(mockAccount);

        // Act
        NewAccountHistory result = newAccountHistoryDAO.getNewAccountByGroundedAccountNumerAndNewAccountNumber(groundedAccountNumber, newAccountNumber, tenantId);

        // Assert
        assertNotNull(result);
        assertEquals(groundedAccountNumber, result.getGroundedAccountNumber());
        assertEquals(newAccountNumber, result.getNewAccountNumber());
        verify(newAccountRepository, times(1))
                .getNewAccountByGroundedAccountNumerAndNewAccountNumber(groundedAccountNumber, newAccountNumber, tenantId);
    }

    @Test
    void testGetNewIneligibleAccountCount() {
        // Arrange
        String groundedAccountNumber = "grounded123";
        String tenantId = "tenant1";
        int mockCount = 5;

        when(newAccountRepository.getNewIneligibleAccountCount(groundedAccountNumber, tenantId)).thenReturn(mockCount);

        // Act
        int result = newAccountHistoryDAO.getNewIneligibleAccountCount(groundedAccountNumber, tenantId);

        // Assert
        assertEquals(mockCount, result);
        verify(newAccountRepository, times(1)).getNewIneligibleAccountCount(groundedAccountNumber, tenantId);
    }
}