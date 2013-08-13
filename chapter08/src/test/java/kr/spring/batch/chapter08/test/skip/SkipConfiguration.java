package kr.spring.batch.chapter08.test.skip;

import kr.spring.batch.chapter08.Product;
import kr.spring.batch.chapter08.ProductFieldSetMapper;
import kr.spring.batch.chapter08.ProductJpaItemWriter;
import kr.spring.batch.chapter08.jpa.repositories.ProductRepository;
import kr.spring.batch.chapter08.skip.DatabaseSkipListener;
import kr.spring.batch.chapter08.skip.ExceptionSkipPolicy;
import kr.spring.batch.chapter08.skip.Slf4jSkipListener;
import kr.spring.batch.chapter08.test.AbstractRobustnessJobConfiguration;
import kr.spring.batch.chapter08.test.JpaHSqlConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.util.ResourceUtils;

import javax.persistence.EntityManagerFactory;
import java.io.FileNotFoundException;

/**
 * kr.spring.batch.chapter08.test.skip.SkipConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 9. 오후 3:09
 */
@Slf4j
@Configuration
@EnableBatchProcessing
@EnableJpaRepositories(basePackageClasses = { ProductRepository.class })
@Import({ JpaHSqlConfiguration.class })
public class SkipConfiguration extends AbstractRobustnessJobConfiguration {

	@Autowired
	EntityManagerFactory emf;

	@Autowired
	ItemReader<Product> productReader;

	@Bean
	public Job importProductsJob() {
		Step importProductsStep = stepBuilders.get("importProductsStep")
		                                      .<Product, Product>chunk(3)
		                                      .reader(productReader)
		                                      .writer(productItemWriter())
		                                      .faultTolerant().skipLimit(2).skip(FlatFileParseException.class)
		                                      .listener(slf4jSkipListener())
		                                      .listener(databaseSkipListener())
		                                      .build();

		return jobBuilders.get("importProductsJob")
		                  .start(importProductsStep)
		                  .build();
	}

	@Bean
	public Job importProductsJobWithSkipPolicy() {
		Step importProductsStepWithSkipPolicy = stepBuilders.get("importProductsStepWithSkipPolicy")
		                                                    .<Product, Product>chunk(3)
		                                                    .faultTolerant().skipPolicy(skipPolicy())
		                                                    .reader(productReader)
		                                                    .writer(productItemWriter())
		                                                    .build();

		return jobBuilders.get("importProductsJobWithSkipPolicy")
		                  .start(importProductsStepWithSkipPolicy)
		                  .build();
	}


	@Bean
	public ExceptionSkipPolicy skipPolicy() {
		return new ExceptionSkipPolicy(FlatFileParseException.class);
	}

	@Bean
	public Slf4jSkipListener slf4jSkipListener() {
		return new Slf4jSkipListener();
	}

	@Bean
	public DatabaseSkipListener databaseSkipListener() {
		return new DatabaseSkipListener();
	}

	@Bean
	@StepScope
	public FlatFileItemReader<Product> productReader(@Value("#{jobParameters['inputFile']}") String inputFile) {

		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
		tokenizer.setNames(new String[] { "PRODUCT_ID", "NAME", "DESCRIPTION", "PRICE" });

		DefaultLineMapper<Product> lineMapper = new DefaultLineMapper<Product>();
		lineMapper.setLineTokenizer(tokenizer);
		lineMapper.setFieldSetMapper(new ProductFieldSetMapper());

		FlatFileItemReader<Product> reader = new FlatFileItemReader<Product>();

		try {
			reader.setResource(new FileSystemResource(ResourceUtils.getFile(inputFile)));
		} catch (FileNotFoundException e) {
			log.error("파일을 찾을 수 없습니다. inputFile=" + inputFile, e);
			throw new RuntimeException(e);
		}
		reader.setLinesToSkip(1);
		reader.setLineMapper(lineMapper);

		return reader;
	}

	@Bean
	public ProductJpaItemWriter productItemWriter() {
		return new ProductJpaItemWriter();
	}
}
