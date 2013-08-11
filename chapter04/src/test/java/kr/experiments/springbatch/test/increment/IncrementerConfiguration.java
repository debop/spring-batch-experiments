package kr.experiments.springbatch.test.increment;

import kr.experiments.springbatch.configuration.LaunchConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

/**
 * kr.experiments.springbatch.test.increment.IncrementerConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오후 4:13
 */
@Slf4j
@Configuration
@EnableBatchProcessing
@Import(LaunchConfiguration.class)
@ImportResource({ "classpath:/spring/incrementer-job.xml" })
public class IncrementerConfiguration {

	@Bean
	public Tasklet tasklet() {
		return Mockito.mock(Tasklet.class);
	}

	@Bean
	public JobParametersIncrementer incrementer() {
		return new RunIdIncrementer();
	}
}
