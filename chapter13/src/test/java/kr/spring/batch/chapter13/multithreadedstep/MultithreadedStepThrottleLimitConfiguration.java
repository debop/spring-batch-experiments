package kr.spring.batch.chapter13.multithreadedstep;

import kr.spring.batch.chapter13.AbstractBatchConfiguration;
import kr.spring.batch.chapter13.DummyProductReader;
import kr.spring.batch.chapter13.DummyProductWriter;
import kr.spring.batch.chapter13.JpaHSqlConfiguration;
import kr.spring.batch.chapter13.domain.Product;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * kr.spring.batch.chapter13.multithreadedstep.MultithreadedStepThrottleLimitConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 25. 오후 7:55
 */
@Configuration
@EnableBatchProcessing
@Import({ JpaHSqlConfiguration.class })
public class MultithreadedStepThrottleLimitConfiguration extends AbstractBatchConfiguration {

	@Bean
	public Job readWriteMultiThreadJob(DummyProductReader reader) {
		Step step = stepBuilders.get("readWriteMultiThreadStep")
		                        .<Product, Product>chunk(10)
		                        .reader(reader)
		                        .writer(writer())
		                        .taskExecutor(taskExecutor())
		                        .throttleLimit(5)
		                        .build();

		return jobBuilders.get("readWriteMultiThreadJob").start(step).build();
	}

	@Bean
	@StepScope
	public DummyProductReader reader(@Value("#{jobParameters['count']}") Integer max) {
		return new DummyProductReader(max);
	}

	@Bean
	@StepScope
	public DummyProductWriter writer() {
		return new DummyProductWriter();
	}
}
