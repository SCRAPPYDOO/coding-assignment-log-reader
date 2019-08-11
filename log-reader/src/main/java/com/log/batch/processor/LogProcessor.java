package com.log.batch.processor;

import com.log.model.LogEntity;
import com.log.batch.dto.LogDto;
import com.log.repository.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Component
public class LogProcessor implements ItemProcessor<LogDto, LogEntity> {

    private static final Logger logger = LoggerFactory.getLogger(LogProcessor.class);

    private final LogRepository logRepository;
    private final long alertDifferenceInMs;

    public LogProcessor(final LogRepository logRepository,
                        @Value("${log.alert.difference_in_ms}") final long alertDifferenceInMs) {
        this.logRepository = logRepository;
        this.alertDifferenceInMs = alertDifferenceInMs;
    }

    @Override
    @Transactional
    public LogEntity process(final LogDto logDto) {


        final Optional<LogEntity> logEntityOptional = logRepository.findFirstById(logDto.getId());

        if(!logEntityOptional.isPresent()) {
            logger.info("Saving event id:{} timestamp:{} state:{}", logDto.getId(), logDto.getTimestamp(), logDto.getState());
            return createLogEntity(logDto);
        }

        logEntityOptional.ifPresent(savedLog -> {
            logger.info("Updating event id:{} timestamp:{} state:{}", logDto.getId(), logDto.getTimestamp(), logDto.getState());
            if(logDto.getState().equals("STARTED")) {
                savedLog.setStarted(logDto.getTimestamp());

                if(Objects.nonNull(savedLog.getFinished())) {
                    long difference = savedLog.getFinished() - logDto.getTimestamp();

                    setDuration(difference, savedLog);
                }
            }

            if(logDto.getState().equals("FINISHED")) {
                savedLog.setFinished(logDto.getTimestamp());

                long difference = logDto.getTimestamp() - savedLog.getStarted();

                setDuration(difference, savedLog);
            }
        });

        return null;
    }

    private LogEntity createLogEntity(final LogDto logDto) {
        final LogEntity newLogEntity = new LogEntity();

        newLogEntity.setId(logDto.getId());
        newLogEntity.setHost(logDto.getHost());
        newLogEntity.setType(logDto.getType());

        if (logDto.getState().equals("STARTED")) {
            newLogEntity.setStarted(logDto.getTimestamp());
        }

        if (logDto.getState().equals("FINISHED")) {
            newLogEntity.setFinished(logDto.getTimestamp());
        }

        return newLogEntity;
    }

    private void setDuration(long difference, final LogEntity savedLog) {
        if (difference > alertDifferenceInMs) {
            savedLog.setAlert(true);
            savedLog.setDuration(difference);
            logger.info("Setting alert on event id:{} started:{} finished:{} duration:{}", savedLog.getId(), savedLog.getStarted(), savedLog.getFinished(), savedLog.getDuration());
        }
    }
}
