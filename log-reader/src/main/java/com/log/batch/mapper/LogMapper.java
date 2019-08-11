package com.log.batch.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.log.batch.dto.LogDto;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.stereotype.Component;

@Component
public class LogMapper implements LineMapper<LogDto> {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public LogDto mapLine(String line, int lineNumber) throws Exception {
        return mapper.readValue(line, LogDto.class);
    }
}
