package kr.spring.batch.chapter08.test.skip;

import kr.spring.batch.chapter08.jpa.repositories.ProductRepository;
import kr.spring.batch.chapter08.test.AbstractRobustnessBatchConfiguration;
import kr.spring.batch.chapter08.test.JpaHSqlConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.EntityManagerFactory;

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
public class SkipBehaviorConfiguration extends AbstractRobustnessBatchConfiguration {

	@Autowired
	EntityManagerFactory emf;

	@Autowired
	ItemReader<String> reader;

	@Autowired
	ItemProcessor<String, String> processor;

	@Autowired
	ItemWriter<String> writer;

	@Autowired
	SkipListener skipListener;

	@Bean
	public Job importProductsJob() {
		Step importProductsStep = stepBuilders.get("importProductsStep")
		                                      .<String, String>chunk(5)
		                                      .reader(reader)
		                                      .processor(processor)
		                                      .writer(writer)
		                                      .faultTolerant()
		                                      .skipLimit(5)
		                                      .skip(FlatFileParseException.class)
		                                      .skip(DataIntegrityViolationException.class)
		                                      .listener(skipListener)
		                                      .build();

		return jobBuilders.get("importProductsJob")
		                  .start(importProductsStep)
		                  .build();
	}

}
