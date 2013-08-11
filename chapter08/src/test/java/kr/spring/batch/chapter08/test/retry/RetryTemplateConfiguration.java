package kr.spring.batch.chapter08.test.retry;

import kr.spring.batch.chapter08.jpa.repositories.ProductRepository;
import kr.spring.batch.chapter08.retry.DiscountService;
import kr.spring.batch.chapter08.retry.DiscountsHolder;
import kr.spring.batch.chapter08.retry.DiscountsTasklet;
import org.mockito.Mockito;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * kr.spring.batch.chapter08.test.retry.RetryTemplateConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 10. 오후 5:03
 */
@Configuration
@EnableAspectJAutoProxy
@EnableBatchProcessing
@EnableJpaRepositories(basePackageClasses = { ProductRepository.class })
public class RetryTemplateConfiguration {

	@Bean
	public RetryOperationsInterceptor retryAdvice() {

		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		retryPolicy.setMaxAttempts(2);

		RetryTemplate retryTemplate = new RetryTemplate();
		retryTemplate.setRetryPolicy(retryPolicy);

		RetryOperationsInterceptor interceptor = new RetryOperationsInterceptor();
		interceptor.setRetryOperations(retryTemplate);

		return interceptor;
	}

	@Bean
	public Tasklet discountsTasklet() {
		DiscountsTasklet tasklet = new DiscountsTasklet();
		tasklet.setDiscountService(discountService());
		tasklet.setDiscountsHolder(discountsHolder());
		return tasklet;
	}

	@Bean
	public DiscountService discountService() {
		return Mockito.mock(DiscountService.class);
	}

	@Bean
	public DiscountsHolder discountsHolder() {
		return Mockito.mock(DiscountsHolder.class);
	}
}
