package kr.spring.batch.chapter07.test;

import kr.spring.batch.chapter07.Product;
import kr.spring.batch.chapter07.ProductFieldSetMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * kr.spring.batch.chapter06.test.FlatFileReaderConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 7. 오전 10:59
 */
@Slf4j
@Configuration
public class FlatFileReaderConfiguration {

    @Bean
    @StepScope
    public FlatFileItemReader<Product> productItemReader(@Value("#{jobParameters['inputFile']}") String inputFile) {
        FlatFileItemReader<Product> reader = new FlatFileItemReader<Product>();

        reader.setResource(new ClassPathResource(inputFile));
        reader.setLinesToSkip(1);
        reader.setLineMapper(productLineMapper());

        return reader;
    }

    @Bean
    public DefaultLineMapper<Product> productLineMapper() {
        DefaultLineMapper<Product> lineMapper = new DefaultLineMapper<Product>();
        lineMapper.setLineTokenizer(productLineTokenizer());
        lineMapper.setFieldSetMapper(productFieldSetMapper());
        return lineMapper;
    }

    @Bean
    public LineTokenizer productLineTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setNames(new String[]{ "PRODUCT_ID", "NAME", "DESCRIPTION", "PRICE" });
        return tokenizer;
    }

    @Bean
    public FieldSetMapper<Product> productFieldSetMapper() {
        return new ProductFieldSetMapper();
    }
}
