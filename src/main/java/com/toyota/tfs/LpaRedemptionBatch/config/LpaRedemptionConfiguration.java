/**
 * 
 */
package com.toyota.tfs.LpaRedemptionBatch.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;

/**
 * @author dasp1
 *
 */
@Slf4j
@Configuration
public class LpaRedemptionConfiguration {

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
	public LocalValidatorFactoryBean validator(MessageSource messageSource) {
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource);
		return bean;
	}
	
	@Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
       return builder.build();
    }

	@Bean
	public WebClient webClient(WebClient.Builder builder) {
		return builder.filter(logWebClientRequest()).build();
	}

	private ExchangeFilterFunction logWebClientRequest() {
		return (clientRequest, next) -> {
			log.info("Request: {} {}", clientRequest.url(), clientRequest.method());
			return next.exchange(clientRequest).doOnNext((ClientResponse response) -> {
				log.info("{} Respond with {} status code", clientRequest.url(), response.rawStatusCode());
			});
		};
	}

}
