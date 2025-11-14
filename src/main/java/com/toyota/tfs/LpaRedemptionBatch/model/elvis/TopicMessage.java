package com.toyota.tfs.LpaRedemptionBatch.model.elvis;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopicMessage {
    private String topic;
    private String key;
    @JsonProperty("value")
    private MessageValue value;
    private int partition;
    private int offset;
}
