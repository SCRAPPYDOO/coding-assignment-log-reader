package com.log.batch.configuration;

import com.log.model.LogEntity;
import com.log.batch.dto.LogDto;
import com.log.batch.processor.LogProcessor;
import com.log.batch.mapper.LogMapper;
import com.log.batch.writer.LogWriter;
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

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private static final String LOG_READER_NAME = "logReader";

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final String logDataPath;

    public BatchConfiguration(final JobBuilderFactory jobBuilderFactory,
                              final StepBuilderFactory stepBuilderFactory,
                              @Value("${log.data.path}") final String logDataPath) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.logDataPath = logDataPath;
    }

    @Bean
    public FlatFileItemReader<LogDto> reader(final LogMapper logLineMapper) {
        return new FlatFileItemReaderBuilder<LogDto>()
                .name(LOG_READER_NAME)
                .resource(new ClassPathResource(logDataPath))
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