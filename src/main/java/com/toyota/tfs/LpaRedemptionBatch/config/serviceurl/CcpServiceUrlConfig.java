package com.toyota.tfs.LpaRedemptionBatch.config.serviceurl;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.toyota.tfs.LpaRedemptionBatch.exception.AccServiceException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties("api.tfs.ccp")
public class CcpServiceUrlConfig extends AbstractServiceUrlConfiguration {
    Map<String,String> attr;
    Map<String,String> item;

    public String getAttr(String value) {
        if (value != null && !value.isEmpty()) {
            if (attr.containsKey(value)) {
                return attr.get(value);
            } else {
                throw new AccServiceException("Invalid attr type for : " + value);
            }
        } else {
            throw new AccServiceException("Attr not read for :  " + value);
        }
    }

    public String getItem(String value) {
        if (value != null && !value.isEmpty()) {
            if (item.containsKey(value)) {
                return item.get(value);
            } else {
                throw new AccServiceException("Invalid item type for : " + value);
            }
        } else {
            throw new AccServiceException("Item not read for :  " + value);
        }
    }
}

