package com.log.reader.configuration;

import javax.sql.DataSource;

import com.log.reader.mapper.LogLineMapper;
import com.log.reader.processor.LogLineProcessor;
import com.log.reader.domain.LogLine;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private static final String INSERT_SQL = "INSERT INTO logs (id, state, type, host, timestamp) VALUES (:id, :state, :type, :host, :timestamp)";

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<LogLine> reader(final LogLineMapper logLineMapper) {
        return new FlatFileItemReaderBuilder<LogLine>()
                .name("logLineReader")
                .resource(new ClassPathResource("sample-data.log"))
                .lineMapper(logLineMapper)
                .build();
    }

    @Bean
    public LogLineMapper logLineMapper() {
        return new LogLineMapper();
    }

    @Bean
    public LogLineProcessor processor() {
        return new LogLineProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<LogLine> writer(final DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<LogLine>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql(INSERT_SQL)
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job loglineReaderJob(final Step step) {
        return jobBuilderFactory.get("loglineReaderJob")
                .incrementer(new RunIdIncrementer())
                .flow(step)
                .end()
                .build();
    }

    @Bean
    public Step step(final JdbcBatchItemWriter<LogLine> writer) {
        return stepBuilderFactory.get("step")
                .<LogLine, LogLine> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }
}