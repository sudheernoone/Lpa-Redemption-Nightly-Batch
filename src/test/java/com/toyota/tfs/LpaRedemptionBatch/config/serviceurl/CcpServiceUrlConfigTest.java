package com.toyota.tfs.LpaRedemptionBatch.config.serviceurl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.toyota.tfs.LpaRedemptionBatch.exception.AccServiceException;

class CcpServiceUrlConfigTest {

    private CcpServiceUrlConfig ccpServiceUrlConfig;

    @BeforeEach
    void setUp() {
        ccpServiceUrlConfig = new CcpServiceUrlConfig();
        Map<String, String> attrMap = new HashMap<>();
        attrMap.put("key1", "value1");
        attrMap.put("key2", "value2");

        Map<String, String> itemMap = new HashMap<>();
        itemMap.put("item1", "value1");
        itemMap.put("item2", "value2");

        ccpServiceUrlConfig.setAttr(attrMap);
        ccpServiceUrlConfig.setItem(itemMap);
    }

    @Test
    void getAttr_ShouldReturnValidValue_WhenKeyExists() {
        String result = ccpServiceUrlConfig.getAttr("key1");
        assertEquals("value1", result, "Expected value for key1 is value1");
    }

    @Test
    void getAttr_ShouldThrowException_WhenKeyDoesNotExist() {
        AccServiceException exception = assertThrows(AccServiceException.class, () -> ccpServiceUrlConfig.getAttr("invalidKey"));
        assertEquals("Invalid attr type for : invalidKey", exception.getMessage());
    }

    @Test
    void getAttr_ShouldThrowException_WhenKeyIsNullOrEmpty() {
        AccServiceException exception = assertThrows(AccServiceException.class, () -> ccpServiceUrlConfig.getAttr(null));
        assertEquals("Attr not read for :  null", exception.getMessage());

        exception = assertThrows(AccServiceException.class, () -> ccpServiceUrlConfig.getAttr(""));
        assertEquals("Attr not read for :  ", exception.getMessage());
    }

    @Test
    void getItem_ShouldReturnValidValue_WhenKeyExists() {
        String result = ccpServiceUrlConfig.getItem("item1");
        assertEquals("value1", result, "Expected value for item1 is value1");
    }

    @Test
    void getItem_ShouldThrowException_WhenKeyDoesNotExist() {
        AccServiceException exception = assertThrows(AccServiceException.class, () -> ccpServiceUrlConfig.getItem("invalidItem"));
        assertEquals("Invalid item type for : invalidItem", exception.getMessage());
    }

    @Test
    void getItem_ShouldThrowException_WhenKeyIsNullOrEmpty() {
        AccServiceException exception = assertThrows(AccServiceException.class, () -> ccpServiceUrlConfig.getItem(null));
        assertEquals("Item not read for :  null", exception.getMessage());

        exception = assertThrows(AccServiceException.class, () -> ccpServiceUrlConfig.getItem(""));
        assertEquals("Item not read for :  ", exception.getMessage());
    }
}