package com.log.reader.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.log.reader.domain.LogLine;
import org.springframework.batch.item.file.LineMapper;

public class LogLineMapper implements LineMapper<LogLine> {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public LogLine mapLine(String line, int lineNumber) throws Exception {
        return mapper.readValue(line, LogLine.class);
    }
}
