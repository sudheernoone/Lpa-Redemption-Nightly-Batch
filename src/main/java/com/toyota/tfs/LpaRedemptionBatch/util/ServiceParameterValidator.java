package com.toyota.tfs.LpaRedemptionBatch.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.toyota.tfs.LpaRedemptionBatch.model.parameter.ServiceParameter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ServiceParameterValidator {

    @Value("${db.serviceparameter.supportedtenant}")
    String supportedTenant;
    @Value("${db.serviceparameter.lpacontractsearchwindow}")
    String lpaContractSearchWindowProp;
    @Value("${db.serviceparameter.lpaprocesswindow}")
    String lpaProcessWindowProp;

    public boolean validateServiceParameterList(List<ServiceParameter> serviceParameterList) throws Exception {
        boolean valid = true;
        int counter = 0;

        if (serviceParameterList == null)
            valid = false;
        else if (serviceParameterList.size() == 0)
            valid = false;
        else {
            for (ServiceParameter serviceParameter : serviceParameterList) {
                if (!isValidTenantId(serviceParameter.getTenantId())) {
                    valid = false;
                    break;
                }
                if (serviceParameter.getKey() != null
                        && serviceParameter.getKey().equalsIgnoreCase(lpaContractSearchWindowProp)) {
                    if (!isNumeric(serviceParameter.getValue())) {
                        valid = false;
                        break;
                    }
                }
                if (serviceParameter.getKey() != null
                        && serviceParameter.getKey().equalsIgnoreCase(lpaProcessWindowProp)) {
                    if (!isNumeric(serviceParameter.getValue())) {
                        valid = false;
                        break;
                    }
                }
            }
        }
        return valid;
    }

    public boolean isValidTenantId(String tenantId) {
        if (supportedTenant != null) {
            String[] tenantIdList = supportedTenant.split(",");
            for (String tenant : tenantIdList) {
                if (tenant.equalsIgnoreCase(tenantId))
                    return true;
            }
		}
		return false;
	}

    public boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional '-' and decimal.
    }
}
