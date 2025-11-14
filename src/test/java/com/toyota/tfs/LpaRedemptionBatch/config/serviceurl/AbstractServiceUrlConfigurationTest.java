package com.toyota.tfs.LpaRedemptionBatch.config.serviceurl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AbstractServiceUrlConfigurationTest {

    private AbstractServiceUrlConfiguration configuration;

    @BeforeEach
    void setUp() {
        configuration = new AbstractServiceUrlConfiguration() {};
        configuration.setHost("http://example.com");
        configuration.setContext("/api");
        configuration.setUrl("http://example.com/api/resource");
    }

    @Test
    void testGetHost() {
        assertEquals("http://example.com", configuration.getHost());
    }

    @Test
    void testGetContext() {
        assertEquals("/api", configuration.getContext());
    }

    @Test
    void testGetUrl() {
        assertEquals("http://example.com/api/resource", configuration.getUrl());
    }
}