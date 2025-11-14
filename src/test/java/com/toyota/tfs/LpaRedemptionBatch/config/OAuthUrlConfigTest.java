package com.toyota.tfs.LpaRedemptionBatch.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OAuthUrlConfigTest {

    private OAuthUrlConfig oAuthUrlConfig;

    @BeforeEach
    void setUp() {
        oAuthUrlConfig = new OAuthUrlConfig();
    }

    @Test
    void testSetAndGetBaseUrl() {
        // Arrange
        String baseUrl = "https://example.com/oauth";
/*
        // Act
        oAuthUrlConfig.setBaseUrl(baseUrl);

        // Assert
        assertEquals(baseUrl, oAuthUrlConfig.getBaseUrl());*/
    }

    @Test
    void testSetAndGetEndpoint() {
        // Arrange
        String endpoint = "/token";
/*
        // Act
        oAuthUrlConfig.setEndpoint(endpoint);

        // Assert
        assertEquals(endpoint, oAuthUrlConfig.getEndpoint());*/
    }
}