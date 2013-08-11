package kr.spring.batch.chapter05.test.file;

import kr.spring.batch.chapter05.AbstractJobConfiguration;
import kr.spring.batch.chapter05.Product;
import kr.spring.batch.chapter05.file.ProductFieldSetMapper;
import kr.spring.batch.chapter05.file.WrappedJsonLineMapper;
import kr.spring.batch.chapter05.test.DummyProductItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.mapping.JsonLineMapper;
import org.springframework.batch.item.file.separator.JsonRecordSeparatorPolicy;
import org.springframework.batch.item.file.separator.RecordSeparatorPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;

/**
 * kr.spring.batch.chapter05.test.file.JobStructureJsonFlatFileConfig
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 1. 오후 4:21
 */
@Configuration
@EnableBatchProcessing
public class JobStructureJsonFlatFileConfig extends AbstractJobConfiguration {

	@Bean
	@Override
	public TaskExecutor jobTaskExecutor() throws Exception {
		return null;
	}

	@Bean
	public Job importProductsJob() throws Exception {

		Step step = stepBuilders.get("importProductsJob")
		                        .<Product, Product>chunk(100)
		                        .reader(productItemReader())
		                        .writer(productItemWriter())
		                        .build();

		return jobBuilders.get("importProductsJob")
		                  .start(step)
		                  .build();
	}

	@Bean
	public ItemReader<Product> productItemReader() {
		FlatFileItemReader<Product> reader = new FlatFileItemReader<Product>();
		reader.setResource(new ClassPathResource("/input/products.json"));
		reader.setRecordSeparatorPolicy(productRecordSeparatorPolicy());
		reader.setLineMapper(productLineMapper());

		return reader;
	}

	@Bean
	public RecordSeparatorPolicy productRecordSeparatorPolicy() {
		return new JsonRecordSeparatorPolicy();
	}

	@Bean
	public ItemWriter<Product> productItemWriter() {
		return new DummyProductItemWriter();
	}

	@Bean
	public LineMapper<Product> productLineMapper() {

		// HINT: Wrapping 을 한 이유는 JSON 을 파싱하더라도 Product 로 빌드하는 코드를 넣어야 하기 때문이다.
		//
		WrappedJsonLineMapper lineMapper = new WrappedJsonLineMapper();
		lineMapper.setDelegate(targetProductsLineMapper());

		return lineMapper;
	}

	@Bean
	public JsonLineMapper targetProductsLineMapper() {
		return new JsonLineMapper();
	}

	@Bean
	public FieldSetMapper<Product> productFieldSetMapper() {
		return new ProductFieldSetMapper();
	}
}

