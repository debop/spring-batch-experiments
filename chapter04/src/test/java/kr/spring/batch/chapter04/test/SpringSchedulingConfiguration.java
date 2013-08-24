package kr.spring.batch.chapter04.test;

import kr.spring.batch.infrastructure.BatchDataSourceConfiguration;
import kr.spring.batch.infrastructure.BatchInfrastructureConfiguration;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import java.util.concurrent.CountDownLatch;

/**
 * kr.spring.batch.chapter04.test.SpringSchedulingConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 24. 오후 11:06
 */
@Configuration
@EnableBatchProcessing
@Import({ BatchInfrastructureConfiguration.class, BatchDataSourceConfiguration.class })
@ImportResource({ "classpath:kr/spring/batch/chapter04/test/spring-scheduling-job.xml" })
public class SpringSchedulingConfiguration {

	@Autowired public JobBuilderFactory jobBuilders;
	@Autowired public StepBuilderFactory stepBuilders;
	@Autowired JobLauncher jobLauncher;

	@Bean
	public Job annotationJob() {
		Step step = stepBuilders.get("annotationStep").tasklet(new CountDownTasklet(annotationCountDownLatch())).build();
		return jobBuilders.get("annotationJob").start(step).build();
	}

	@Bean
	public CountDownLatch annotationCountDownLatch() {
		return new CountDownLatch(2);
	}

	@Bean
	public SpringSchedulingLauncher springSchedulingAnnotationLauncher() {
		SpringSchedulingLauncher launcher = new SpringSchedulingLauncher();
		launcher.setJob(annotationJob());
		launcher.setJobLauncher(jobLauncher);

		return launcher;
	}

}
