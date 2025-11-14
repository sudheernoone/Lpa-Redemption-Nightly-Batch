package com.toyota.tfs.LpaRedemptionBatch.config.serviceurl;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public abstract class AbstractServiceUrlConfiguration

{

	String host;
	String context;
	String url;

}
