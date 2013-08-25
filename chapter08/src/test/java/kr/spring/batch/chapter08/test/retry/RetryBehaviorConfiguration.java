package kr.spring.batch.chapter08.test.retry;

import kr.spring.batch.chapter08.jpa.repositories.ProductRepository;
import kr.spring.batch.chapter08.retry.Slf4jRetryListener;
import kr.spring.batch.chapter08.test.AbstractRobustnessBatchConfiguration;
import kr.spring.batch.chapter08.test.JpaHSqlConfiguration;
import org.mockito.Mockito;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.RetryListener;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;

import java.util.HashMap;
import java.util.Map;

/**
 * kr.spring.batch.chapter08.test.retry.RetryBehaviorConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 12. 오전 1:03
 */
@Configuration
@EnableAspectJAutoProxy
@EnableBatchProcessing
@EnableJpaRepositories(basePackageClasses = { ProductRepository.class })
@Import({ JpaHSqlConfiguration.class })
public class RetryBehaviorConfiguration extends AbstractRobustnessBatchConfiguration {

	@Bean
	public Job job() {
		Step step = stepBuilders.get("step")
		                        .<String, String>chunk(5)
		                        .reader(reader())
		                        .processor(processor())
		                        .writer(writer())
		                        .faultTolerant().retryLimit(3).skipLimit(3)
		                        .retry(OptimisticLockingFailureException.class)
		                        .retry(DeadlockLoserDataAccessException.class)
		                        .skip(DeadlockLoserDataAccessException.class)
		                        .listener(mockRetryListener())
		                        .listener(retryListener())
		                        .build();

		return jobBuilders.get("job").start(step).build();
	}

	@Bean
	public Slf4jRetryListener retryListener() {
		return new Slf4jRetryListener();
	}

	@Bean
	public RetryListener mockRetryListener() {
		return Mockito.mock(RetryListener.class);
	}

	@Bean
	public Job retryPolicyJob() {
		Step step = stepBuilders.get("retryPolicyStep")
		                        .<String, String>chunk(5)
		                        .reader(reader())
		                        .writer(writer())
		                        .faultTolerant().retryPolicy(retryPolicy())
		                        .build();

		return jobBuilders.get("retryPolicyJob").start(step).build();
	}

	@Bean
	public ExceptionClassifierRetryPolicy retryPolicy() {

		Map<Class<? extends Throwable>, RetryPolicy> policyMap = new HashMap<>();
		policyMap.put(ConcurrencyFailureException.class, simpleRetryPolicy(3));
		policyMap.put(DeadlockLoserDataAccessException.class, simpleRetryPolicy(5));

		ExceptionClassifierRetryPolicy retryPolicy = new ExceptionClassifierRetryPolicy();
		retryPolicy.setPolicyMap(policyMap);
		return retryPolicy;
	}

	public SimpleRetryPolicy simpleRetryPolicy(int maxAttempts) {
		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		retryPolicy.setMaxAttempts(maxAttempts);
		return retryPolicy;
	}
}
