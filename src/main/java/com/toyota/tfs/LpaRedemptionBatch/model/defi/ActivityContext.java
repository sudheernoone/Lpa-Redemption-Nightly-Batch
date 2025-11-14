package com.toyota.tfs.LpaRedemptionBatch.model.defi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityContext {
	
	private String userId;
	private String activityId;

}
