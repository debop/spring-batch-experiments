package kr.spring.batch.chapter07.test.process;

import kr.spring.batch.chapter07.ExistingProductFilterItemProcessor;
import kr.spring.batch.chapter07.Product;
import kr.spring.batch.chapter07.jpa.ProductRepository;
import kr.spring.batch.chapter07.test.AbstractJobConfiguration;
import kr.spring.batch.chapter07.test.FlatFileReaderConfiguration;
import kr.spring.batch.chapter07.test.JpaHSqlConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.EntityManagerFactory;

/**
 * kr.spring.batch.chapter07.test.process.FilteringExistingProductConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 8. 오후 5:44
 */
@Slf4j
@Configuration
@EnableJpaRepositories(basePackageClasses = { ProductRepository.class })
@EnableBatchProcessing
@Import({ FlatFileReaderConfiguration.class, JpaHSqlConfiguration.class })
public class FilteringExistingProductConfiguration extends AbstractJobConfiguration {

    @Autowired
    EntityManagerFactory emf;

    @Autowired
    FlatFileItemReader<Product> reader;

    @Bean
    public Job readWriteJob() {
        return jobBuilders.get("readWriteJob")
                          .start(readWriteStep())
                          .build();
    }

    @Bean
    public Step readWriteStep() {
        return stepBuilders.get("readWrite")
                           .<Product, Product>chunk(2)
                           .reader(reader)
                           .processor(processor())
                           .writer(writer())
                           .build();
    }

    @Bean
    public JpaPagingItemReader<Product> reader() {
        assert emf != null;
        JpaPagingItemReader<Product> reader = new JpaPagingItemReader<Product>();
        reader.setEntityManagerFactory(emf);
        reader.setPageSize(3);
        reader.setQueryString("select p from Product p");

        return reader;
    }

    @Bean
    public ItemProcessor<Product, Product> processor() {
        return new ExistingProductFilterItemProcessor();
    }

    @Bean
    @StepScope
    public JpaItemWriter<Product> writer() {
        JpaItemWriter<Product> writer = new JpaItemWriter<Product>();
        writer.setEntityManagerFactory(emf);
        return writer;
    }
}
