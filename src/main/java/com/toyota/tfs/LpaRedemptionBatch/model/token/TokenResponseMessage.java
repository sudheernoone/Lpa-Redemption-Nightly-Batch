/**
 * 
 */
package com.toyota.tfs.LpaRedemptionBatch.model.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author dasp1
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseMessage {

	private String status;
	private TokenResponseError Error;
}
