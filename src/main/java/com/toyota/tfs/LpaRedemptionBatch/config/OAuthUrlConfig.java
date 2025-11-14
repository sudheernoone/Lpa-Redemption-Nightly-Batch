package com.toyota.tfs.LpaRedemptionBatch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.toyota.tfs.LpaRedemptionBatch.config.serviceurl.AbstractServiceUrlConfiguration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties("api.tfs.oauth")
public class OAuthUrlConfig extends AbstractServiceUrlConfiguration {

}
