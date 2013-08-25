package kr.spring.batch.chapter13;

import kr.spring.batch.infrastructure.BatchDataSourceConfiguration;
import kr.spring.batch.infrastructure.BatchInfrastructureConfiguration;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * kr.spring.batch.chapter13.AbstractBatchConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 25. 오후 7:20
 */
@Configuration
@EnableBatchProcessing
@Import({ BatchInfrastructureConfiguration.class, BatchDataSourceConfiguration.class })
public abstract class AbstractBatchConfiguration {

	@Autowired protected StepBuilderFactory stepBuilders;
	@Autowired protected JobBuilderFactory jobBuilders;

	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(5);
		taskExecutor.setMaxPoolSize(5);

		return taskExecutor;
	}
}
