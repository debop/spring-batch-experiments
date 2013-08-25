package kr.spring.batch.chapter06.test;

import kr.spring.batch.chapter06.Product;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * kr.spring.batch.chapter06.test.FlatFileReaderConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 7. 오전 10:59
 */
@Configuration
@EnableBatchProcessing
public class FlatFileReaderConfiguration {

	@Bean
	public FlatFileItemReader<Product> productItemReader() {
		FlatFileItemReader<Product> reader = new FlatFileItemReader<Product>();
		reader.setResource(new ClassPathResource("/springbatch/input/products-delimited.txt"));
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
		tokenizer.setNames(new String[] { "id", "name", "description", "price", "operation" });
		return tokenizer;
	}

	@Bean
	public FieldSetMapper<Product> productFieldSetMapper() {
		BeanWrapperFieldSetMapper<Product> mapper = new BeanWrapperFieldSetMapper<Product>();
		mapper.setTargetType(Product.class);
		return mapper;
	}
}
