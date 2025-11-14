package com.toyota.tfs.LpaRedemptionBatch.config.serviceurl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class SparcServiceUrlConfigTest {

    @Test
    void sparcServiceUrlConfig_ShouldBeInitialized() {
        SparcServiceUrlConfig sparcServiceUrlConfig = new SparcServiceUrlConfig();
        assertNotNull(sparcServiceUrlConfig, "SparcServiceUrlConfig object should not be null");
    }
}