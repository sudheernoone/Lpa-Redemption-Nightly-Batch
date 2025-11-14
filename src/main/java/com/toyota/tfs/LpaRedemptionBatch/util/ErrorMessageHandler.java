package com.toyota.tfs.LpaRedemptionBatch.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class ErrorMessageHandler {

    @Autowired
    private Environment env;

    public interface UTIL {
    }

    public interface MESSAGE_CODE {
        static final String SUCCESS_STATUS="Success";
        static final String ERROR_STATUS="Error";
        
        
        static final String ACCOUNT_NOT_FOUND = "ERRLPAN001";
        static final String PRIMARY_BORROWER_SSN_NOT_FOUND = "ERRLPAN002";
       
        static final String CCP_API_CALL_FAILED= "ERRLPAN003";
        static final String SPARC_API_CALL_FAILED= "ERRLPAN004";
        static final String DEFI_API_CALL_FAILED= "ERRLPAN005";
        
        static final String TERM_EXPIRED = "ERRLPAN006";
        
        static final String DISPOFEE_WAIVER_REASON = "ERRLPAN007";
        
        static final String LPA_WAIVER_INELIGIBLE = "ERRLPAN008";
        static final String NO_VALID_CCP_OFFER= "ERRLPAN009";
        
        static final String NO_NEW_CONTRACT= "ERRLPAN010";
        
        static final String DEALER_DECISION_NOT_FOUND = "ERRLPAN011";
        static final String DEALER_PURCHASE_TYPE_NOT_ELIGIBLE = "ERRLPAN012";
        static final String DEALER_PURCHASE_TYPE_NOT_VALID= "ERRLPAN013";
        static final String TRANSPORTATION_STATUS_NOT_VALID= "ERRLPAN014";
        
        static final String LPA_NOT_ELIGIBLE= "ERRLPAN015";
        
        static final String INVALID_TOKEN="ERRLPAN016";
        
    }
    
    public interface REDEMPTION_REJECTION_REASON {
        static final String DEALER_PURCHASE_TYPE_INELIGIBLE="D";
        static final String MODEL_DOES_NOT_QUALIFY="M";
        static final String CRITERIA_NOT_MET = "C";
    }

    public String getErrorMessage(String errorCode){
        return env.getProperty(errorCode);
    }
}
