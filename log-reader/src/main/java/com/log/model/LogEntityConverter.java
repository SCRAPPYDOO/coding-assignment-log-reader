package com.log.model;

import com.log.batch.dto.LogDto;
import org.springframework.stereotype.Component;

@Component
public class LogEntityConverter {

    public LogEntity convertToStartedEvent(final LogDto logDto) {
        final LogEntity logEntity = new LogEntity();
        logEntity.setId(logDto.getId());
        logEntity.setStarted(logDto.getTimestamp());
        logEntity.setHost(logDto.getHost());
        logEntity.setType(logDto.getType());
        return logEntity;
    }
}
