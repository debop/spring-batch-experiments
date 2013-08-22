package kr.spring.batch.chapter03.test;

import kr.spring.batch.chapter03.Product;
import kr.spring.batch.chapter03.ProductFieldSetMapper;
import kr.spring.batch.chapter03.ProductPrepareStatementSetter;
import kr.spring.batch.chapter03.listener.ImportProductsExecutionListener;
import kr.spring.batch.chapter03.listener.ImportProductsJobListener;
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
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

/**
 * kr.spring.batch.chapter03.test.JobStructureConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오후 1:09
 */
@Slf4j
@Configuration
@EnableBatchProcessing
@ComponentScan(basePackageClasses = { ProductFieldSetMapper.class })
@Import({ LaunchConfiguration.class })
public class JobStructureConfiguration {

	@Autowired
	DataSource dataSource;

	@Autowired
	JobBuilderFactory jobBuilders;

	@Autowired
	StepBuilderFactory stepBuilders;

	@Bean
	public Job importProductsJob() throws Exception {
		Step step = stepBuilders.get("readWrite")
		                        .<Product, Product>chunk(100)
		                        .reader(productItemReader())
		                        .writer(productItemWriter())
		                        .listener(readWriteStepListener())
		                        .build();

		return jobBuilders.get("importProductsJob")
		                  .listener(importProductsJobListener())
		                  .start(step)
		                  .build();
	}

	@Bean
	public ImportProductsExecutionListener readWriteStepListener() {
		return new ImportProductsExecutionListener();
	}

	@Bean
	public ImportProductsJobListener importProductsJobListener() {
		return new ImportProductsJobListener();
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
		lineMapper.setFieldSetMapper(new ProductFieldSetMapper());
		lineMapper.afterPropertiesSet();
		return lineMapper;
	}

	@Bean
	public LineTokenizer productLineTokenizer() {
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
		tokenizer.setNames(new String[] { "id", "name", "description", "price" });
		return tokenizer;
	}

	@Bean
	public ItemWriter<Product> productItemWriter() {
		JdbcBatchItemWriter<Product> itemWriter = new JdbcBatchItemWriter<Product>();
		itemWriter.setDataSource(dataSource);
		itemWriter.setSql("insert into product (id, name, description, price) values(?,?,?,?)");
		itemWriter.setItemPreparedStatementSetter(new ProductPrepareStatementSetter());
		itemWriter.afterPropertiesSet();

		return itemWriter;
	}
}
