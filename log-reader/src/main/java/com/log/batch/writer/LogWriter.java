package com.log.batch.writer;

import com.log.model.LogEntity;
import com.log.repository.LogRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class LogWriter implements ItemWriter<LogEntity> {

    private final LogRepository logRepository;

    public LogWriter(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    @Transactional
    public void write(List<? extends LogEntity> items) {
        logRepository.saveAll(items);
    }
}
