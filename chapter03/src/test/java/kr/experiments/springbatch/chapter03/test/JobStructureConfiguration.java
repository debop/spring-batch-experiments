package kr.experiments.springbatch.chapter03.test;

import kr.experiments.springbatch.chapter03.Product;
import kr.experiments.springbatch.chapter03.ProductPrepareStatementSetter;
import kr.experiments.springbatch.chapter03.listener.ImportProductsExecutionListener;
import kr.experiments.springbatch.chapter03.listener.ImportProductsJobListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

/**
 * kr.experiments.springbatch.chapter03.test.JobStructureConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오후 1:09
 */
@Slf4j
@Configuration
@EnableBatchProcessing
@Import(LaunchConfiguration.class)
public class JobStructureConfiguration {

    @Autowired
    DataSource dataSource;

    @Autowired
    JobBuilderFactory jobBuilders;

    @Autowired
    StepBuilderFactory stepBuilders;


    @Bean
    public ImportProductsExecutionListener importProductsExecutionListener() {
        return new ImportProductsExecutionListener();
    }

    @Bean
    public ImportProductsJobListener importProductsJobListener() {
        return new ImportProductsJobListener();
    }

    @Bean
    public ImportProductsExecutionListener readWriteStepListener() {
        return new ImportProductsExecutionListener();
    }

    @Bean
    public ItemReader<Product> productItemReader() throws Exception {
        FlatFileItemReader<Product> reader = new FlatFileItemReader<Product>();
        reader.setResource(new ClassPathResource("/input/products-delimited.txt"));
        reader.setLinesToSkip(1);
        reader.setLineMapper(productLineMapper());

        reader.afterPropertiesSet();
        return reader;
    }

    @Bean
    public LineMapper<Product> productLineMapper() throws Exception {
        DefaultLineMapper<Product> lineMapper = new DefaultLineMapper<Product>();
        lineMapper.setLineTokenizer(productLineTokenizer());
        lineMapper.setFieldSetMapper(productFieldSetMapper());
        lineMapper.afterPropertiesSet();
        return lineMapper;
    }

    @Bean
    public LineTokenizer productLineTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setNames(new String[]{ "id", "name", "description", "price" });
        return tokenizer;
    }

    @Bean
    public FieldSetMapper productFieldSetMapper() throws Exception {
        BeanWrapperFieldSetMapper<Product> fieldSetMapper = new BeanWrapperFieldSetMapper<Product>();
        fieldSetMapper.setPrototypeBeanName("product");
        fieldSetMapper.afterPropertiesSet();

        return fieldSetMapper;
    }

    @Bean
    @Scope("prototype")
    public Product product() {
        return new Product();
    }

    @Bean
    public ProductPrepareStatementSetter productPrepareStatementSetter() {
        return new ProductPrepareStatementSetter();
    }

    @Bean
    public ItemWriter<Product> productItemWriter() {
        JdbcBatchItemWriter<Product> itemWriter = new JdbcBatchItemWriter<Product>();
        itemWriter.setDataSource(dataSource);
        itemWriter.setSql("insert into product (id, name, description, price) values(?,?,?,?)");
        itemWriter.setItemPreparedStatementSetter(productPrepareStatementSetter());
        itemWriter.afterPropertiesSet();

        return itemWriter;
    }

    @Bean
    public Job importProductsJob() throws Exception {

        Step readWrite = stepBuilders.get("readWrite")
                                     .listener(importProductsExecutionListener())
                                     .<Product, Product>chunk(100)
                                     .reader(productItemReader())
                                     .writer(productItemWriter())

                                     .build();

        Job importProducts = jobBuilders.get("importProductsJob")
                                        .listener(importProductsJobListener())
                                        .start(readWrite)
                                        .build();

        return importProducts;
    }
}
