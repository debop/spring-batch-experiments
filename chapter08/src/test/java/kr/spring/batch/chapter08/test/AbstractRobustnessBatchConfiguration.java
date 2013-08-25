package kr.spring.batch.chapter08.test;

import kr.spring.batch.chapter08.test.beans.BusinessService;
import kr.spring.batch.chapter08.test.beans.DummyItemProcessor;
import kr.spring.batch.chapter08.test.beans.DummyItemReader;
import kr.spring.batch.chapter08.test.beans.DummyItemWriter;
import kr.spring.batch.infrastructure.BatchDataSourceConfiguration;
import kr.spring.batch.infrastructure.BatchInfrastructureConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * kr.experiments.springbatch.AbstractJobConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 1. 오전 9:35
 */
@Slf4j
@Configuration
@EnableBatchProcessing
@Import({ BatchInfrastructureConfiguration.class, BatchDataSourceConfiguration.class })
public class AbstractRobustnessBatchConfiguration {

	@Autowired
	protected JobBuilderFactory jobBuilders;

	@Autowired
	protected StepBuilderFactory stepBuilders;

	@Autowired
	protected JobExplorer jobExplorer;

	@Autowired
	protected JobOperator jobOperator;

	@Autowired
	protected JobRegistry jobRegistry;

	@Bean
	public BusinessService service() {
		return Mockito.mock(BusinessService.class);
	}

	@Bean
	public ItemReader<String> reader() {
		return new DummyItemReader(service());
	}

	@Bean
	public ItemProcessor<String, String> processor() {
		return new DummyItemProcessor(service());
	}

	@Bean
	public DummyItemWriter writer() {
		return new DummyItemWriter(service());
	}

	@Bean
	public SkipListener skipListener() {
		return Mockito.mock(SkipListener.class);
	}
}
