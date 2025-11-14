package com.toyota.tfs.LpaRedemptionBatch.model.token;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseError {
    private String code;
    private String message;

}
