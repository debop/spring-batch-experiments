package kr.spring.batch.chapter02.test.structure;

import kr.spring.batch.chapter02.config.RootDatabaseConfiguration;
import kr.spring.batch.chapter02.domain.Product;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * kr.spring.batch.chapter02.test.structure.JobStructureSimpleConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오전 10:27
 */
@Configuration
@EnableBatchProcessing
@Import({ RootDatabaseConfiguration.class })
public class JobStructureSimpleConfiguration {

    private static final String[] FIELD_NAMES = new String[]{ "PRODUCT_ID", "NAME", "DESCRIPTION", "PRICE" };

    private static final String OVERRIDDEN_BY_EXPRESSION = null;

    @Autowired
    JobBuilderFactory jobBuilders;

    @Autowired
    StepBuilderFactory stepBuilders;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    JobRegistry jobRegistry;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    DataSource dataSource;

    @Autowired
    JobExecutionListener loggerListener;

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public JobExecutionDecider skippedDecider() {
        return new SkippedDecider();
    }

    @Bean
    ItemReader<Product> reader() {
        return new DummyItemReader();
    }

    @Bean
    public ItemWriter<Product> writer() {
        return new DummyItemWriter();
    }

    @Bean
    public Tasklet dummyTasklet() {
        return new DummyTasklet();
    }

    @Bean
    public Job importProductsJob(Tasklet dummyTasklet, ItemReader<Product> reader) {

        Step decompress = stepBuilders.get("decompress")
                                      .tasklet(dummyTasklet)
                                      .repository(jobRepository)
                                      .transactionManager(transactionManager)
                                      .build();

        Step readWrite = stepBuilders.get("readWriteProducts")
                                     .<Product, Product>chunk(100)
                                     .reader(reader)
                                     .writer(writer())
                                     .faultTolerant()
                                     .skipLimit(5)
                                     .skip(FlatFileParseException.class)
                                     .build();

        Step clean = stepBuilders.get("clean")
                                 .tasklet(dummyTasklet)
                                 .repository(jobRepository)
                                 .transactionManager(transactionManager)
                                 .build();

        return jobBuilders.get("importProductsJob")
                          .repository(jobRepository)
                          .listener(loggerListener)
                          .start(decompress)
                          .next(readWrite)
                          .next(clean)
                          .build();
    }

}
