package com.log.reader.domain;

public class LogLine {
    private final String id;
    private final String state;
    private final String type;
    private final String host;
    private final long timestamp;

    public LogLine(String id, String state, String type, String host, long timestamp) {
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
