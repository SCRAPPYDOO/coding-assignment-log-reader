package com.log.batch.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LogDto {
    private final String id;
    private final String state;
    private final String type;
    private final String host;
    private final long timestamp;

    @JsonCreator
    public LogDto(@JsonProperty(value="id") String id,
                  @JsonProperty(value="state") String state,
                  @JsonProperty(value="type") String type,
                  @JsonProperty(value="host") String host,
                  @JsonProperty(value="timestamp") long timestamp) {
        this.id = id;
        this.state = state;
        this.type = type;
        this.host = host;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getType() {
        return type;
    }

    public String getHost() {
        return host;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
