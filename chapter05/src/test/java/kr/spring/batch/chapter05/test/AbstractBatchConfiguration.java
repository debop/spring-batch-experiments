package kr.spring.batch.chapter05.test;

import kr.spring.batch.infrastructure.BatchDataSourceConfiguration;
import kr.spring.batch.infrastructure.BatchInfrastructureConfiguration;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * kr.spring.batch.chapter05.test.AbstractBatchConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 25. 오전 2:39
 */
@Configuration
@EnableBatchProcessing
@Import({ BatchInfrastructureConfiguration.class, BatchDataSourceConfiguration.class })
public abstract class AbstractBatchConfiguration {

	@Autowired
	public StepBuilderFactory stepBuilders;

	@Autowired
	public JobBuilderFactory jobBuilders;

}
