package com.toyota.tfs.LpaRedemptionBatch.model.token;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class OauthTokenResponseTest {

    @Test
    void testDefaultConstructor() {
        // Act
        OauthTokenResponse response = new OauthTokenResponse();

        // Assert
        assertNotNull(response);
        assertNull(response.getToken_type());
        assertNull(response.getExpires_in());
        assertNull(response.getAccess_token());
        assertNull(response.getScope());
    }

    @Test
    void testParameterizedConstructor() {
        // Arrange
        String tokenType = "Bearer";
        String expiresIn = "3600";
        String accessToken = "abc123";
        String scope = "read";

        // Act
        OauthTokenResponse response = new OauthTokenResponse(tokenType, expiresIn, accessToken, scope);

        // Assert
        assertNotNull(response);
        assertEquals(tokenType, response.getToken_type());
        assertEquals(expiresIn, response.getExpires_in());
        assertEquals(accessToken, response.getAccess_token());
        assertEquals(scope, response.getScope());
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        OauthTokenResponse response = new OauthTokenResponse();
        String tokenType = "Bearer";
        String expiresIn = "7200";
        String accessToken = "xyz789";
        String scope = "write";

        // Act
        response.setToken_type(tokenType);
        response.setExpires_in(expiresIn);
        response.setAccess_token(accessToken);
        response.setScope(scope);

        // Assert
        assertEquals(tokenType, response.getToken_type());
        assertEquals(expiresIn, response.getExpires_in());
        assertEquals(accessToken, response.getAccess_token());
        assertEquals(scope, response.getScope());
    }
}