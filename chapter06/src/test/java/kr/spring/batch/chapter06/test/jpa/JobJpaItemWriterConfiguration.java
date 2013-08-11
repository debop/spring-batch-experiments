package kr.spring.batch.chapter06.test.jpa;

import kr.spring.batch.chapter06.Product;
import kr.spring.batch.chapter06.jpa.ProductRepository;
import kr.spring.batch.chapter06.test.AbstractJobConfiguration;
import kr.spring.batch.chapter06.test.FlatFileReaderConfiguration;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.EntityManagerFactory;

/**
 * kr.spring.batch.chapter06.test.jpa.JobJpaItemWriterConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 7. 오후 2:52
 */
@Configuration
@EnableBatchProcessing
@EnableJpaRepositories(basePackageClasses = { ProductRepository.class })
@Import({ FlatFileReaderConfiguration.class, JpaHSqlConfiguration.class })
public class JobJpaItemWriterConfiguration extends AbstractJobConfiguration {

	@Autowired
	protected EntityManagerFactory emf;

	@Bean
	public Job writeProductsJob(Step readWriteStep) {
		return jobBuilders.get("writeProductsJob")
		                  .start(readWriteStep)
		                  .build();
	}

	@Bean
	public Step readWriteStep(ItemReader<Product> productItemReader) {
		return
				stepBuilders.get("readWrite")
				            .<Product, Product>chunk(3)
				            .reader(productItemReader)
				            .writer(productItemWriter())
				            .build();
	}

	// HINT: JPAItemWriter 가 있으므로 굳이 만들 필요가 없습니다.
	@Bean
	public JpaItemWriter<Product> productItemWriter() {
		JpaItemWriter<Product> itemWriter = new JpaItemWriter<Product>();
		itemWriter.setEntityManagerFactory(emf);

		return itemWriter;
	}
}
