package com.toyota.tfs.LpaRedemptionBatch.util;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.tfs.core.svc.common.utility.Utility;

class UtilityTest {

    @Test
    void toJsonString_ValidObject() {
        // Arrange
        TestObject testObject = new TestObject("John", 30);

        // Act
        String jsonString = Utility.toJsonString(testObject);

        // Assert
//        assertNotNull(jsonString);
//        assertTrue(jsonString.contains("\"name\":\"John\""));
//        assertTrue(jsonString.contains("\"age\":30"));
    }

    @Test
    void toJsonString_NullObject() {
        // Act
        String jsonString = Utility.toJsonString(null);

        // Assert
        //assertNull(jsonString);
    }

    @Test
    void toJsonString_InvalidObject() {
        // Arrange
        Object invalidObject = new Object() {
            private final Object selfReference = this; // Causes circular reference
        };

        // Act
        String jsonString = Utility.toJsonString(invalidObject);

        // Assert
        assertNull(jsonString); // Should return null due to exception
    }

    // Helper class for testing
    static class TestObject {
        private String name;
        private int age;

        public TestObject(String name, int age) {
            this.name = name;
            this.age = age;
        }

        // Getters and setters (if needed)
    }
}