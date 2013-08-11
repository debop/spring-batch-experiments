package kr.spring.batch.chapter07.test.process;

import kr.spring.batch.chapter07.ExistingProductFilterItemProcessor;
import kr.spring.batch.chapter07.Product;
import kr.spring.batch.chapter07.ProductFieldSetMapper;
import kr.spring.batch.chapter07.jpa.ProductRepository;
import kr.spring.batch.chapter07.test.AbstractJobConfiguration;
import kr.spring.batch.chapter07.test.JpaHSqlConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
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
@Import({ JpaHSqlConfiguration.class })
public class FilteringExistingProductConfiguration extends AbstractJobConfiguration {

	@Autowired
	EntityManagerFactory emf;

	@Bean
	public Job readWriteJob(Step readWriteStep) {
		return jobBuilders.get("readWriteJob")
		                  .start(readWriteStep)
		                  .build();
	}

	@Bean
	public Step readWriteStep(FlatFileItemReader<Product> reader) {
		return stepBuilders.get("readWrite")
		                   .<Product, Product>chunk(2)
		                   .reader(reader)
		                   .processor(processor())
		                   .writer(writer())
		                   .build();
	}

	@Bean
	@StepScope
	public FlatFileItemReader<Product> reader(@Value("#{jobParameters['inputFile']}") String inputFile) {
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
		tokenizer.setNames(new String[] { "PRODUCT_ID", "NAME", "DESCRIPTION", "PRICE" });
		return tokenizer;
	}

	@Bean
	public FieldSetMapper<Product> productFieldSetMapper() {
		return new ProductFieldSetMapper();
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
