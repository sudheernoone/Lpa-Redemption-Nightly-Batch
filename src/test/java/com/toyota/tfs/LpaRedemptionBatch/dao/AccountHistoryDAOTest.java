package com.toyota.tfs.LpaRedemptionBatch.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.toyota.tfs.LpaRedemptionBatch.model.dao.AccountHistory;
import com.toyota.tfs.LpaRedemptionBatch.repository.AccountHistoryRepository;

@ExtendWith(MockitoExtension.class)
class AccountHistoryDAOTest {

    @Mock
    private AccountHistoryRepository accountHistoryRepository;

    @InjectMocks
    private AccountHistoryDAO accountHistoryDAO;

    @BeforeEach
    void setUp() {
        // Initialize mocks
    }

    @Test
    void testGetAccount() {
        // Arrange
        String accountNumber = "12345";
        String tenantId = "tenant1";
        AccountHistory mockAccount = new AccountHistory();
        mockAccount.setAccountNumber(accountNumber);

        when(accountHistoryRepository.getAccountByAccountNumber(accountNumber, tenantId)).thenReturn(mockAccount);

        // Act
        AccountHistory result = accountHistoryDAO.getAccount(accountNumber, tenantId);

        // Assert
        assertNotNull(result);
        assertEquals(accountNumber, result.getAccountNumber());
        verify(accountHistoryRepository, times(1)).getAccountByAccountNumber(accountNumber, tenantId);
    }

    @Test
    void testFetchNewContractForPrimaryBorrowerAsBorrower() {
        // Arrange
        String primaryBorrowerSsnTin = "123-45-6789";
        String maxDate = "2023-12-31";
        String minDate = "2023-01-01";
        String accountNumber = "12345";
        String tenantId = "tenant1";

        AccountHistory mockAccount = new AccountHistory();
        mockAccount.setAccountNumber(accountNumber);
        List<AccountHistory> mockAccounts = Arrays.asList(mockAccount);

        when(accountHistoryRepository.getAccountByPrimaryBorrowerSsnTin(primaryBorrowerSsnTin, maxDate, minDate, accountNumber, tenantId))
                .thenReturn(mockAccounts);

        // Act
        List<AccountHistory> result = accountHistoryDAO.fetchNewContractForPrimaryBorrowerAsBorrower(primaryBorrowerSsnTin, maxDate, minDate, accountNumber, tenantId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(accountNumber, result.get(0).getAccountNumber());
        verify(accountHistoryRepository, times(1)).getAccountByPrimaryBorrowerSsnTin(primaryBorrowerSsnTin, maxDate, minDate, accountNumber, tenantId);
    }

    @Test
    void testFetchNewContractForPrimaryBorrowerAsCoBorrower() {
        // Arrange
        String primaryBorrowerSsnTin = "123-45-6789";
        String maxDate = "2023-12-31";
        String minDate = "2023-01-01";
        String accountNumber = "12345";
        String tenantId = "tenant1";

        AccountHistory mockAccount = new AccountHistory();
        mockAccount.setAccountNumber(accountNumber);
        List<AccountHistory> mockAccounts = Arrays.asList(mockAccount);

        when(accountHistoryRepository.getAccountByCoBorrowerSsn(primaryBorrowerSsnTin, maxDate, minDate, accountNumber, tenantId))
                .thenReturn(mockAccounts);

        // Act
        List<AccountHistory> result = accountHistoryDAO.fetchNewContractForPrimaryBorrowerAsCoBorrower(primaryBorrowerSsnTin, maxDate, minDate, accountNumber, tenantId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(accountNumber, result.get(0).getAccountNumber());
        verify(accountHistoryRepository, times(1)).getAccountByCoBorrowerSsn(primaryBorrowerSsnTin, maxDate, minDate, accountNumber, tenantId);
    }

    @Test
    void testFetchNewContractForCoBorrowerAsBorrower() {
        // Arrange
        String coBorrowerSsn = "987-65-4321";
        String maxDate = "2023-12-31";
        String minDate = "2023-01-01";
        String accountNumber = "12345";
        String tenantId = "tenant1";

        AccountHistory mockAccount = new AccountHistory();
        mockAccount.setAccountNumber(accountNumber);
        List<AccountHistory> mockAccounts = Arrays.asList(mockAccount);

        when(accountHistoryRepository.getAccountByPrimaryBorrowerSsnTin(coBorrowerSsn, maxDate, minDate, accountNumber, tenantId))
                .thenReturn(mockAccounts);

        // Act
        List<AccountHistory> result = accountHistoryDAO.fetchNewContractForCoBorrowerAsBorrower(coBorrowerSsn, maxDate, minDate, accountNumber, tenantId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(accountNumber, result.get(0).getAccountNumber());
        verify(accountHistoryRepository, times(1)).getAccountByPrimaryBorrowerSsnTin(coBorrowerSsn, maxDate, minDate, accountNumber, tenantId);
    }

    @Test
    void testFetchNewContractForCoBorrowerAsCoBorrower() {
        // Arrange
        String coBorrowerSsn = "987-65-4321";
        String maxDate = "2023-12-31";
        String minDate = "2023-01-01";
        String accountNumber = "12345";
        String tenantId = "tenant1";

        AccountHistory mockAccount = new AccountHistory();
        mockAccount.setAccountNumber(accountNumber);
        List<AccountHistory> mockAccounts = Arrays.asList(mockAccount);

        when(accountHistoryRepository.getAccountByCoBorrowerSsn(coBorrowerSsn, maxDate, minDate, accountNumber, tenantId))
                .thenReturn(mockAccounts);

        // Act
        List<AccountHistory> result = accountHistoryDAO.fetchNewContractForCoBorrowerAsCoBorrower(coBorrowerSsn, maxDate, minDate, accountNumber, tenantId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(accountNumber, result.get(0).getAccountNumber());
        verify(accountHistoryRepository, times(1)).getAccountByCoBorrowerSsn(coBorrowerSsn, maxDate, minDate, accountNumber, tenantId);
    }
}