package kr.spring.batch.chapter08.retry;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.List;

/**
 * kr.spring.batch.chapter08.retry.DiscountsWithRetryTemplateTasklet
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 10. 오후 9:33
 */
@Slf4j
public class DiscountsWithRetryTemplateTasklet implements Tasklet {

	@Setter
	private DiscountService discountService;
	@Setter
	private DiscountsHolder discountsHolder;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		RetryTemplate retryTemplate = new RetryTemplate();
		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		retryPolicy.setMaxAttempts(3);
		retryTemplate.setRetryPolicy(retryPolicy);

		// HINT: 재시도 정책을 지정해서 3번까지 재시도를 수행합니다.
		//
		List<Discount> discounts = retryTemplate.execute(new RetryCallback<List<Discount>>() {
			@Override
			public List<Discount> doWithRetry(RetryContext context) throws Exception {
				return discountService.getDiscounts();
			}
		});

		discountsHolder.setDiscounts(discounts);
		return RepeatStatus.FINISHED;
	}
}
