package kr.spring.batch.chapter05.test.file;

import kr.spring.batch.chapter05.AbstractJobConfiguration;
import kr.spring.batch.chapter05.Product;
import kr.spring.batch.chapter05.file.ProductFieldSetMapper;
import kr.spring.batch.chapter05.file.TwoLineProductRecordSeparatorPolicy;
import kr.spring.batch.chapter05.test.DummyProductItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.separator.RecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;

/**
 * kr.spring.batch.chapter05.test.file.JobStructureDelimitedTwoLinesFlatFileConfig
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 1. 오후 4:09
 */
@Configuration
@EnableBatchProcessing
public class JobStructureDelimitedTwoLinesFlatFileConfig extends AbstractJobConfiguration {

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
		reader.setResource(new ClassPathResource("/input/products-delimited-two-lines.txt"));
		reader.setLinesToSkip(1);
		reader.setLineMapper(productLineMapper());
		reader.setRecordSeparatorPolicy(recordSeparatorPolicy());
		return reader;
	}

	@Bean
	public ItemWriter<Product> productItemWriter() {
		return new DummyProductItemWriter();
	}

	@Bean
	public LineMapper<Product> productLineMapper() {
		DefaultLineMapper<Product> mapper = new DefaultLineMapper<Product>();
		mapper.setLineTokenizer(productLineTokenizer());
		mapper.setFieldSetMapper(productFieldSetMapper());

		return mapper;
	}

	@Bean
	public RecordSeparatorPolicy recordSeparatorPolicy() {
		return new TwoLineProductRecordSeparatorPolicy();
	}

	@Bean
	public LineTokenizer productLineTokenizer() {
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
		tokenizer.setNames(new String[] { "id", "name", "description", "price" });
		return tokenizer;
	}

	@Bean
	public FieldSetMapper<Product> productFieldSetMapper() {
		return new ProductFieldSetMapper();
	}
}