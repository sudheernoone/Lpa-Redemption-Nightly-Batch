package com.toyota.tfs.LpaRedemptionBatch.util;

import static com.toyota.tfs.LpaRedemptionBatch.util.ErrorMessageHandler.MESSAGE_CODE.INVALID_TOKEN;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.toyota.tfs.LpaRedemptionBatch.config.OAuthUrlConfig;
import com.toyota.tfs.LpaRedemptionBatch.exception.UnauthorizedException;
import com.toyota.tfs.LpaRedemptionBatch.model.token.TokenResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TokenValidator {

    @Autowired
    OAuthUrlConfig oAuthUrlConfig;
    @Autowired
    ErrorMessageHandler errorMessageHandler;
    @Autowired
    private RestTemplate restTemplate;

    public void validateToken(String token) throws UnauthorizedException {
        StringBuilder serviceUrl = new StringBuilder("https://");
        serviceUrl.append(oAuthUrlConfig.getHost());
        serviceUrl.append(oAuthUrlConfig.getContext());
        serviceUrl.append(oAuthUrlConfig.getUrl());
        if (StringUtils.isNotBlank(token)) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("token", token.replaceFirst("Bearer ", ""));
            try {
                HttpEntity<Void> requestEntity = new HttpEntity<>(httpHeaders);
                ResponseEntity<TokenResponse> validateTokenResponse = restTemplate.exchange(serviceUrl.toString(), HttpMethod.GET, requestEntity,
                        TokenResponse.class);
                boolean validToken = false;
                if (validateTokenResponse.getStatusCode().is2xxSuccessful()) {
                    /*List<String> tokenScope = validateTokenResponse.getBody().getScp();
                    for (String s : tokenScope) {
                        if (s.equals(oAuthUrlConfig.getScope())) {
                            validToken = true;
                            break;
                        }
                    }
                    if (!validToken) {
                        log.error("Token does not have the scope required to access the API");
                        throw new UnauthorizedException(MESSAGE_CODE.INVALID_TOKEN,
                        errorMessageHandler.getErrorMessage(MESSAGE_CODE.INVALID_TOKEN));
                    }
                */} else {
                    log.error("Error while validating the OAuth Token with API Gateway.");
                    throw new UnauthorizedException(INVALID_TOKEN,
                            errorMessageHandler.getErrorMessage(INVALID_TOKEN));
                }
            } catch (Exception e) {e.printStackTrace();
                log.error("Error calling the API Gateway Token Validation Service with the given token:" + token);
                throw new UnauthorizedException(INVALID_TOKEN,
                        errorMessageHandler.getErrorMessage(INVALID_TOKEN));
            }
        }else{
            log.error("No token has been used to call the API");
            throw new UnauthorizedException(INVALID_TOKEN,
                    errorMessageHandler.getErrorMessage(INVALID_TOKEN));
        }
    }
}
