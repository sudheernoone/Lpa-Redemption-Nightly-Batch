package com.toyota.tfs.LpaRedemptionBatch.config.serviceurl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class DefiServiceUrlConfigTest {

    @Test
    void defiServiceUrlConfig_ShouldBeInitialized() {
        DefiServiceUrlConfig defiServiceUrlConfig = new DefiServiceUrlConfig();
        assertNotNull(defiServiceUrlConfig, "DefiServiceUrlConfig object should not be null");
    }
}