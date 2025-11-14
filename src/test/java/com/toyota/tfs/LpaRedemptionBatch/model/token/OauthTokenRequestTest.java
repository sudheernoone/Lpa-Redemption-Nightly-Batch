package com.toyota.tfs.LpaRedemptionBatch.model.token;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class OauthTokenRequestTest {

    @Test
    void testDefaultConstructor() {
        // Act
        OauthTokenRequest request = new OauthTokenRequest();

        // Assert
        assertNotNull(request);
        assertNull(request.getGrant_type());
        assertNull(request.getClient_id());
        assertNull(request.getClient_secret());
    }

    @Test
    void testParameterizedConstructor() {
        // Arrange
        String grantType = "password";
        String clientId = "client123";
        String clientSecret = "secret123";

        // Act
        OauthTokenRequest request = new OauthTokenRequest(grantType, clientId, clientSecret);

        // Assert
        assertNotNull(request);
        assertEquals(grantType, request.getGrant_type());
        assertEquals(clientId, request.getClient_id());
        assertEquals(clientSecret, request.getClient_secret());
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        OauthTokenRequest request = new OauthTokenRequest();
        String grantType = "client_credentials";
        String clientId = "client456";
        String clientSecret = "secret456";

        // Act
        request.setGrant_type(grantType);
        request.setClient_id(clientId);
        request.setClient_secret(clientSecret);

        // Assert
        assertEquals(grantType, request.getGrant_type());
        assertEquals(clientId, request.getClient_id());
        assertEquals(clientSecret, request.getClient_secret());
    }
}