package kr.spring.batch.chapter08.test.retry;

import kr.spring.batch.chapter08.retry.Discount;
import kr.spring.batch.chapter08.retry.DiscountService;
import kr.spring.batch.chapter08.retry.DiscountsHolder;
import kr.spring.batch.chapter08.retry.DiscountsWithRetryTemplateTasklet;
import org.junit.Test;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * {@link org.springframework.retry.support.RetryTemplate}를 이용한 테스트
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 10. 오후 5:03
 */
@ContextConfiguration(classes = { RetryTemplateConfiguration.class })
public class RetryTemplateTest {

	@Test
	public void discountTaskletWithRetryTemplateOk() throws Exception {
		DiscountsWithRetryTemplateTasklet tasklet = new DiscountsWithRetryTemplateTasklet();
		DiscountService service = mock(DiscountService.class);
		DiscountsHolder holder = new DiscountsHolder();
		tasklet.setDiscountService(service);
		tasklet.setDiscountsHolder(holder);

		List<Discount> discounts = new ArrayList<Discount>();
		discounts.add(new Discount());

		// HINT: Mockito를 이용하여, 두번의 실퍠 후 성공하는 가상 시나리오를 제작합니다.
		when(service.getDiscounts())
				.thenThrow(new TransientDataAccessResourceException(""))
				.thenThrow(new TransientDataAccessResourceException(""))
				.thenReturn(discounts);

		RepeatStatus status = tasklet.execute(null, null);
		assertThat(status).isEqualTo(RepeatStatus.FINISHED);
		assertThat(holder.getDiscounts()).isEqualTo(discounts);
	}
}
