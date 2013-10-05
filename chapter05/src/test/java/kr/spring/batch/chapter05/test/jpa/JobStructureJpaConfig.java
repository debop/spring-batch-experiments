package kr.spring.batch.chapter05.test.jpa;

import kr.spring.batch.chapter05.Product;
import kr.spring.batch.chapter05.test.AbstractBatchConfiguration;
import kr.spring.batch.chapter05.test.DummyProductItemWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;

/**
 * kr.spring.batch.chapter05.test.jpa.JobStructureJpaCursorConfig
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 1. 오후 5:38
 */
@Slf4j
@Configuration
@EnableBatchProcessing
@EnableTransactionManagement
@Import(HSqlConfig.class)
// @ImportResource({ "classpath:kr/spring/batch/chapter05/test/jpa/jobstructure-jpa.xml" })
public class JobStructureJpaConfig extends AbstractBatchConfiguration {

    @Autowired EntityManagerFactory emf;

    @Bean
    public Job importProductsJob() throws Exception {

        Step step = stepBuilders.get("importProductsStep")
                                .<Product, Product>chunk(100)
                                .reader(productItemReader())
                                .writer(productItemWriter())
                                .build();

        return jobBuilders.get("importProductsJob").start(step).build();
    }

    @Bean
    public ItemReader<Product> productItemReader() throws Exception {
        JpaPagingItemReader<Product> reader = new JpaPagingItemReader<Product>();
        reader.setEntityManagerFactory(emf);
        reader.setQueryString("select p from Product p");
        reader.setPageSize(5);
        reader.afterPropertiesSet();

        return reader;
    }

    @Bean
    public ItemWriter<Product> productItemWriter() {
        return new DummyProductItemWriter();
    }
}
