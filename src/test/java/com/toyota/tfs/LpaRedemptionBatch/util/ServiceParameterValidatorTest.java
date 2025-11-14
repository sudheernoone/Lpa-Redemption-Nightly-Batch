package com.toyota.tfs.LpaRedemptionBatch.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;

import com.toyota.tfs.LpaRedemptionBatch.model.parameter.ServiceParameter;

class ServiceParameterValidatorTest {

    @InjectMocks
    private ServiceParameterValidator serviceParameterValidator;

    @Value("${db.serviceparameter.supportedtenant}")
    private String supportedTenant = "tenant1,tenant2";

    @Value("${db.serviceparameter.lpacontractsearchwindow}")
    private String lpaContractSearchWindowProp = "contractSearchWindow";

    @Value("${db.serviceparameter.lpaprocesswindow}")
    private String lpaProcessWindowProp = "processWindow";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        serviceParameterValidator.supportedTenant = supportedTenant;
        serviceParameterValidator.lpaContractSearchWindowProp = lpaContractSearchWindowProp;
        serviceParameterValidator.lpaProcessWindowProp = lpaProcessWindowProp;
    }

    @Test
    void testValidateServiceParameterList_Valid() throws Exception {
        // Arrange
        List<ServiceParameter> serviceParameterList = new ArrayList<>();
        ServiceParameter param1 = new ServiceParameter("tenant1", "contractSearchWindow", "30","","","","","");
        ServiceParameter param2 = new ServiceParameter("tenant2", "processWindow", "60", "30","","","","");
        serviceParameterList.add(param1);
        serviceParameterList.add(param2);

        // Act
        boolean isValid = serviceParameterValidator.validateServiceParameterList(serviceParameterList);

        // Assert
        assertTrue(!isValid);
    }

    @Test
    void testValidateServiceParameterList_InvalidTenant() throws Exception {
        // Arrange
        List<ServiceParameter> serviceParameterList = new ArrayList<>();
        ServiceParameter param = new ServiceParameter("invalidTenant", "contractSearchWindow", "30","","","","","");
        serviceParameterList.add(param);

        // Act
        boolean isValid = serviceParameterValidator.validateServiceParameterList(serviceParameterList);

        // Assert
        assertFalse(isValid);
    }



    @Test
    void testValidateServiceParameterList_InvalidNumericValue() throws Exception {
        // Arrange
        List<ServiceParameter> serviceParameterList = new ArrayList<>();
        ServiceParameter param = new ServiceParameter("tenant1", "contractSearchWindow", "invalid","","","","","");
        serviceParameterList.add(param);

        // Act
        boolean isValid = serviceParameterValidator.validateServiceParameterList(serviceParameterList);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void testIsValidTenantId_Valid() {
        // Act
        boolean isValid = serviceParameterValidator.isValidTenantId("tenant1");

        // Assert
        assertTrue(isValid);
    }

    @Test
    void testIsValidTenantId_Invalid() {
        // Act
        boolean isValid = serviceParameterValidator.isValidTenantId("invalidTenant");

        // Assert
        assertFalse(isValid);
    }

    @Test
    void testIsNumeric_Valid() {
        // Act
        boolean isNumeric = serviceParameterValidator.isNumeric("123");

        // Assert
        assertTrue(isNumeric);
    }

    @Test
    void testIsNumeric_Invalid() {
        // Act
        boolean isNumeric = serviceParameterValidator.isNumeric("abc");

        // Assert
        assertFalse(isNumeric);
    }

    @Test
    void validateServiceParameterList() {
        List<ServiceParameter> serviceParameterList= new ArrayList<>();
        serviceParameterList.add(new ServiceParameter("1","tenant1", "LPA", "lpaContractSearchWindowProp","30","","",""));
        serviceParameterList.add(new ServiceParameter("1","tenant1", "LPA", "lpaProcessWindowProp","30","","",""));
        supportedTenant="tenant1,tenant2";
        lpaContractSearchWindowProp="lpaContractSearchWindowProp";
        lpaProcessWindowProp="lpaProcessWindowProp";
        try {
            boolean result = serviceParameterValidator.validateServiceParameterList(serviceParameterList);
            assertTrue(result);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    void validateServiceParameterListFail1() {
        List<ServiceParameter> serviceParameterList= new ArrayList<>();
        serviceParameterList.add(new ServiceParameter("1","tenant1", "LPA", "lpaContractSearchWindowProp","abc","","",""));
        serviceParameterList.add(new ServiceParameter("1","tenant1", "LPA", "lpaProcessWindowProp","abc","","",""));
        serviceParameterValidator.supportedTenant="tenant1,tenant2";
        serviceParameterValidator.lpaContractSearchWindowProp="lpaContractSearchWindowProp";
        serviceParameterValidator.lpaProcessWindowProp="lpaProcessWindowProp";
        try {
            boolean result = serviceParameterValidator.validateServiceParameterList(serviceParameterList);
            assertFalse(result);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    void validateServiceParameterListFail2() {
        List<ServiceParameter> serviceParameterList= new ArrayList<>();
        serviceParameterList.add(new ServiceParameter("1","tenant1", "LPA", "lpaContractSearchWindowProp","30","","",""));
        serviceParameterList.add(new ServiceParameter("1","tenant1", "LPA", "lpaProcessWindowProp","abc","","",""));
        serviceParameterValidator.supportedTenant="tenant1,tenant2";
        serviceParameterValidator.lpaContractSearchWindowProp="lpaContractSearchWindowProp";
        serviceParameterValidator.lpaProcessWindowProp="lpaProcessWindowProp";
        try {
            boolean result = serviceParameterValidator.validateServiceParameterList(serviceParameterList);
            assertFalse(result);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    void validateServiceParameterListFail3() {
        List<ServiceParameter> serviceParameterList= new ArrayList<>();
        serviceParameterValidator.supportedTenant="tenant1,tenant2";
        serviceParameterValidator.lpaContractSearchWindowProp="lpaContractSearchWindowProp";
        serviceParameterValidator.lpaProcessWindowProp="lpaProcessWindowProp";
        try {
            boolean result = serviceParameterValidator.validateServiceParameterList(serviceParameterList);
            assertFalse(result);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    void validateServiceParameterListFail4() {
        List<ServiceParameter> serviceParameterList= null;
        serviceParameterValidator.supportedTenant="tenant1,tenant2";
        serviceParameterValidator.lpaContractSearchWindowProp="lpaContractSearchWindowProp";
        serviceParameterValidator.lpaProcessWindowProp="lpaProcessWindowProp";
        try {
            boolean result = serviceParameterValidator.validateServiceParameterList(serviceParameterList);
            assertFalse(result);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
}