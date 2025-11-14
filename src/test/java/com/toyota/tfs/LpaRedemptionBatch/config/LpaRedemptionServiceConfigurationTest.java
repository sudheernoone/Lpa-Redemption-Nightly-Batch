package com.toyota.tfs.LpaRedemptionBatch.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
@ExtendWith(MockitoExtension.class)
class LpaRedemptionServiceConfigurationTest {

    private LpaRedemptionConfiguration configuration;

    @BeforeEach
    void setUp() {
        configuration = new LpaRedemptionConfiguration();
    }

    @Test
    void messageSource_ShouldReturnMessageSource() {
        // Act
        MessageSource messageSource = configuration.messageSource();

        // Assert
        assertNotNull(messageSource);
        assert(messageSource instanceof ReloadableResourceBundleMessageSource);
    }

    @Test
    void validator_ShouldReturnValidator() {
        // Arrange
        MessageSource messageSource = configuration.messageSource();

        // Act
        LocalValidatorFactoryBean validator = configuration.validator(messageSource);

        // Assert
        assertNotNull(validator);
    }

    @Test
    void restTemplate_ShouldReturnRestTemplate() {
        // Arrange
        RestTemplateBuilder builder = mock(RestTemplateBuilder.class);
        builder = new RestTemplateBuilder();

        // Act
        RestTemplate restTemplate = configuration.restTemplate(builder);

        // Assert
        assertNotNull(restTemplate);
    }

    @Test
    void webClient_ShouldReturnWebClient() {
        // Arrange
        WebClient.Builder builder = WebClient.builder();

        // Act
        WebClient webClient = configuration.webClient(builder);

        // Assert
        assertNotNull(webClient);
    }


    @Test
    public void testLogWebClientRequest() throws Exception {
        ExchangeFilterFunction exchangeFilterFunction =
                ReflectionTestUtils.invokeMethod(configuration, "logWebClientRequest");
        assertNotNull(exchangeFilterFunction);
    }

}