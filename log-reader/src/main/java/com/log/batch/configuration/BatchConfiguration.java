package com.log.batch.configuration;

import com.log.model.LogEntity;
import com.log.batch.dto.LogDto;
import com.log.batch.processor.LogProcessor;
import com.log.batch.mapper.LogMapper;
import com.log.batch.writer.LogWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.Objects;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);
    private static final String LOG_READER_NAME = "logReader";
    private static final String DEFAULT_LOG_DATA = "sample-data";

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final Resource resource;

    public BatchConfiguration(final JobBuilderFactory jobBuilderFactory,
                              final StepBuilderFactory stepBuilderFactory,
                              @Value("${log.data.path}") final String logDataPath,
                              final ResourceLoader resourceLoader) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;

        if(Objects.isNull(logDataPath) || logDataPath.isEmpty()) {
            logger.warn("property log.data.path is not set or its empty, loading default data set");
            this.resource = new ClassPathResource(DEFAULT_LOG_DATA);
        } else {
            this.resource = resourceLoader.getResource(String.format("file:%s", logDataPath));
        }
    }

    @Bean
    public FlatFileItemReader<LogDto> reader(final LogMapper logLineMapper) {
        return new FlatFileItemReaderBuilder<LogDto>()
                .name(LOG_READER_NAME)
                .resource(resource)
                .lineMapper(logLineMapper)
                .build();
    }

    @Bean
    public Job logLineReaderJob(final Step step) {
        return jobBuilderFactory.get("logLineReaderJob")
                .incrementer(new RunIdIncrementer())
                .flow(step)
                .end()
                .build();
    }

    @Bean
    public Step step(final LogProcessor logProcessor, final LogWriter logWriter, final LogMapper logMapper) {
        return stepBuilderFactory.get("step")
                .<LogDto, LogEntity>chunk(1)
                .reader(reader(logMapper))
                .processor(logProcessor)
                .writer(logWriter)
                .build();
    }
}