package com.toyota.tfs.LpaRedemptionBatch.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.toyota.tfs.LpaRedemptionBatch.exception.ServiceParameterException;
import com.toyota.tfs.LpaRedemptionBatch.model.parameter.ServiceParameter;
import com.toyota.tfs.LpaRedemptionBatch.repository.ServiceParameterRepository;
import com.toyota.tfs.LpaRedemptionBatch.util.ServiceParameterValidator;

@ExtendWith(MockitoExtension.class)
class ServiceParamterServiceTest {

    @Mock
    private ServiceParameterRepository serviceParameterRepository;

    @Mock
    private ServiceParameterValidator serviceParameterValidator;

    @InjectMocks
    private ServiceParamterService serviceParamterService;

    private List<ServiceParameter> serviceParameterList;

    @BeforeEach
    void setUp() {
        serviceParameterList = new ArrayList<>();
        serviceParameterList.add(new ServiceParameter("1", "tenant1", "LeasePullAhead", "key1", "value1", "Y", "user1", "2023-10-01"));
        serviceParameterList.add(new ServiceParameter("2", "tenant2", "LeasePullAhead", "key2", "value2", "Y", "user2", "2023-10-02"));
    }

    @Test
    void testGetRecord_Valid() throws Exception {
        // Arrange
        when(serviceParameterRepository.findByServiceNameAndActiveFlag("LeasePullAhead", "Y")).thenReturn(serviceParameterList);
        when(serviceParameterValidator.validateServiceParameterList(serviceParameterList)).thenReturn(true);

        // Act
        List<ServiceParameter> result = serviceParamterService.getRecord();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(serviceParameterRepository, times(1)).findByServiceNameAndActiveFlag("LeasePullAhead", "Y");
        verify(serviceParameterValidator, times(1)).validateServiceParameterList(serviceParameterList);
    }

    @Test
    void testGetRecord_Invalid() throws Exception {
        // Arrange
        when(serviceParameterRepository.findByServiceNameAndActiveFlag("LeasePullAhead", "Y")).thenReturn(serviceParameterList);
        when(serviceParameterValidator.validateServiceParameterList(serviceParameterList)).thenReturn(false);

        // Act & Assert
        assertThrows(ServiceParameterException.class, () -> serviceParamterService.getRecord());
        verify(serviceParameterRepository, times(1)).findByServiceNameAndActiveFlag("LeasePullAhead", "Y");
        verify(serviceParameterValidator, times(1)).validateServiceParameterList(serviceParameterList);
    }

    @Test
    void testGetRecordsByTenantIdAndKey_Valid() {
        // Act
        String result = serviceParamterService.getRecordsByTenantIdAndKey(serviceParameterList, "tenant1", "key1");

        // Assert
        assertNotNull(result);
        assertEquals("value1", result);
    }

    @Test
    void testGetRecordsByTenantIdAndKey_Invalid() {
        // Act
        String result = serviceParamterService.getRecordsByTenantIdAndKey(serviceParameterList, "invalidTenant", "key1");

        // Assert
        assertNull(result);
    }
}