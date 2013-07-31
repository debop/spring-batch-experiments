package kr.experiments.springbatch.chapter03.test;

import kr.experiments.springbatch.chapter03.Product;
import kr.experiments.springbatch.chapter03.ProductFieldSetMapper;
import kr.experiments.springbatch.chapter03.ProductPrepareStatementSetter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
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
@ComponentScan(basePackageClasses = { ProductFieldSetMapper.class })
@Import(LaunchConfiguration.class)
@ImportResource("classpath:/spring/job.xml")
public class JobStructureConfiguration {

    @Autowired
    DataSource dataSource;

    @Autowired
    JobBuilderFactory jobBuilders;

    @Autowired
    StepBuilderFactory stepBuilders;


    @Bean
    public ItemReader<Product> productItemReader(LineMapper<Product> productLineMapper) throws Exception {
        FlatFileItemReader<Product> reader = new FlatFileItemReader<Product>();
        reader.setResource(new ClassPathResource("/input/products-delimited.txt"));
        reader.setLinesToSkip(1);
        reader.setLineMapper(productLineMapper);

        reader.afterPropertiesSet();
        return reader;
    }

    @Bean
    public LineMapper<Product> productLineMapper(FieldSetMapper<Product> productFieldSetMapper) throws Exception {
        DefaultLineMapper<Product> lineMapper = new DefaultLineMapper<Product>();
        lineMapper.setLineTokenizer(productLineTokenizer());
        lineMapper.setFieldSetMapper(productFieldSetMapper);
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
    public ItemWriter<Product> productItemWriter(ProductPrepareStatementSetter productPrepareStatementSetter) {
        JdbcBatchItemWriter<Product> itemWriter = new JdbcBatchItemWriter<Product>();
        itemWriter.setDataSource(dataSource);
        itemWriter.setSql("insert into product (id, name, description, price) values(?,?,?,?)");
        itemWriter.setItemPreparedStatementSetter(productPrepareStatementSetter);
        itemWriter.afterPropertiesSet();

        return itemWriter;
    }

//    @Bean
//    public Job importProductsJob() throws Exception {
//
//        Step readWrite = stepBuilders.get("readWrite")
//                                     .listener(importProductsExecutionListener())
//                                     .<Product, Product>chunk(100)
//                                     .reader(productItemReader())
//                                     .writer(productItemWriter())
//
//                                     .build();
//
//        Job importProducts = jobBuilders.get("importProductsJob")
//                                        .listener(importProductsJobListener())
//                                        .start(readWrite)
//                                        .build();
//
//        return importProducts;
//    }
}
