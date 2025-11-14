package com.toyota.tfs.LpaRedemptionBatch.config.serviceurl;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties("api.tfs.sparc")
public class SparcServiceUrlConfig extends AbstractServiceUrlConfiguration {
}

