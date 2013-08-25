package kr.spring.batch.chapter07.test.process;

import kr.spring.batch.chapter07.Product;
import kr.spring.batch.chapter07.jpa.ProductRepository;
import kr.spring.batch.chapter07.test.AbstractBatchConfiguration;
import kr.spring.batch.chapter07.test.JpaHSqlConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.EntityManagerFactory;
import java.util.Date;
import java.util.HashMap;

/**
 * kr.spring.batch.chapter07.test.process.ItemProcessorConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 8. 오후 3:26
 */
@Slf4j
@Configuration
@EnableBatchProcessing
@EnableJpaRepositories(basePackageClasses = { ProductRepository.class })
@Import({ JpaHSqlConfiguration.class })
public class DrivingQueryConfiguration extends AbstractBatchConfiguration {

	@Autowired
	EntityManagerFactory emf;

	@Bean
	public Job readWriteJob(Step readWriteStep) {
		return jobBuilders.get("readWriteJob")
		                  .start(readWriteStep)
		                  .build();
	}

	@Bean
	public Step readWriteStep(JpaPagingItemReader<Product> reader, FlatFileItemWriter<Product> writer) {
		return stepBuilders.get("readWrite")
		                   .<Product, Product>chunk(2)
		                   .reader(reader)
		                   .writer(writer)
		                   .build();
	}

	// NOTE: jobParameters 인자를 받으려면 @StepScope 이어야 합니다.
	@Bean
	@StepScope
	public JpaPagingItemReader<Product> reader(@Value("#{jobParameters['updateTimestamp']}") Date updateTiemstamp) {
		assert emf != null;
		log.info("updateTimestamp=[{}]", updateTiemstamp);

		JpaPagingItemReader<Product> reader = new JpaPagingItemReader<Product>();
		reader.setEntityManagerFactory(emf);
		reader.setPageSize(3);
		reader.setQueryString("select p from Product p where p.updateTimestamp > :updateTimestamp");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("updateTimestamp", updateTiemstamp);
		//params.put("updateTimestamp", DateTime.now().withDate(2010, 6, 30).toDate());
		reader.setParameterValues(params);

		return reader;
	}

	// NOTE: jobParameters 인자를 받으려면 @StepScope 이어야 합니다.
	@Bean
	@StepScope
	public FlatFileItemWriter<Product> writer(@Value("#{jobParameters['targetFile']}") String targetFile) {
		log.info("create ItemWriter. file=[{}]", targetFile);

		FlatFileItemWriter<Product> writer = new FlatFileItemWriter<Product>();
		writer.setResource(new FileSystemResource(targetFile));
		writer.setLineAggregator(new PassThroughLineAggregator<Product>());

		return writer;
	}
}
