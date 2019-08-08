package com.log.reader.processor;

import com.log.reader.domain.LogLine;
import org.springframework.batch.item.ItemProcessor;

public class LogLineProcessor implements ItemProcessor<LogLine, LogLine> {

    @Override
    public LogLine process(LogLine item) throws Exception {
        return null;
    }
}
