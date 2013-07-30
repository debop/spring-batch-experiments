package kr.experiments.springbatch.chapter01.config;

import kr.experiments.springbatch.chapter01.batch.DecompressTasklet;
import kr.experiments.springbatch.chapter01.batch.ProductFieldSetMapper;
import kr.experiments.springbatch.chapter01.batch.ProductJdbcItemWriter;
import kr.experiments.springbatch.chapter01.domain.Product;
import kr.experiments.springbatch.config.AbstractBatchConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * kr.experiments.springbatch.chapter01.config.ImportProductConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 30. 오후 3:13
 */
@Slf4j
@Configuration
@EnableBatchProcessing
@ImportResource("classpath:job-context.xml")
public class ImportProductConfiguration extends AbstractBatchConfiguration {

    private static final String[] FIELD_NAMES = new String[]{ "PRODUCT_ID", "NAME", "DESCRIPTION", "PRICE" };

    @Autowired
    protected JobBuilderFactory jobBuilders;

    @Autowired
    protected StepBuilderFactory stepBuilders;

    @Autowired
    protected JobRepository jobRepository;

    @Autowired
    protected JobRegistry jobRegistry;

    @Autowired
    protected PlatformTransactionManager transactionManager;


    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    @StepScope
    public DecompressTasklet decompressTasklet() {
        return new DecompressTasklet();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Product> reader() {
        FlatFileItemReader<Product> reader = new FlatFileItemReader<Product>();

        reader.setLinesToSkip(1);
        reader.setLineMapper(lineMapper());

        return reader;
    }

    @Bean
    @StepScope
    public DefaultLineMapper<Product> lineMapper() {
        DefaultLineMapper<Product> mapper = new DefaultLineMapper<Product>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(FIELD_NAMES);
        mapper.setLineTokenizer(tokenizer);

        ProductFieldSetMapper fieldSetMapper = new ProductFieldSetMapper();
        mapper.setFieldSetMapper(fieldSetMapper);

        return mapper;
    }

    @Bean
    @StepScope
    public ProductJdbcItemWriter writer() {
        return new ProductJdbcItemWriter(dataSource());
    }

//    @Bean
//    public Job importProductsJob() {
//
//        Step decompress = stepBuilders.get("decompress")
//                                      .tasklet(decompressTasklet())
//                                      .repository(jobRepository)
//                                      .transactionManager(transactionManager)
//                                      .build();
//
//        Step readWriteProducts = stepBuilders.get("readWriteProducts")
//                                             .<Product, Product>chunk(3)
//                                             .reader(reader())
//                                             .writer(writer())
//                                             .build();
//
//        return jobBuilders.get("importProductsJob")
//                          .repository(jobRepository)
//                          .listener(jobLoggerListener())
//                          .start(decompress)
//                          .next(readWriteProducts)
//                          .build();
//    }
}
