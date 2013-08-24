package kr.spring.batch.chapter04.test.incrementer;

import kr.spring.batch.infrastructure.BatchDataSourceConfiguration;
import kr.spring.batch.infrastructure.BatchInfrastructureConfiguration;
import org.mockito.Mockito;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * kr.spring.batch.chapter04.test.incrementer.IncrementerConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 24. 오후 1:27
 */
@Configuration
@EnableBatchProcessing
@Import({ BatchInfrastructureConfiguration.class, BatchDataSourceConfiguration.class })
public class IncrementerConfiguration {

	@Autowired StepBuilderFactory stepBuilderFactory;
	@Autowired JobBuilderFactory jobBuilderFactory;

	@Bean
	public Job job() {
		Step step = stepBuilderFactory.get("step").tasklet(tasklet()).build();
		return jobBuilderFactory.get("job").incrementer(incrementer()).start(step).build();
	}

	@Bean
	public Tasklet tasklet() {
		return Mockito.mock(Tasklet.class);
	}

	@Bean
	public RunIdIncrementer incrementer() {
		return new RunIdIncrementer();
	}
}
