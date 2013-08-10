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
 * kr.spring.batch.chapter08.test.retry.RetryTemplateTest
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

        when(service.getDiscounts())
                .thenThrow(new TransientDataAccessResourceException(""))
                .thenThrow(new TransientDataAccessResourceException(""))
                .thenReturn(discounts);

        RepeatStatus status = tasklet.execute(null, null);
        assertThat(status).isEqualTo(RepeatStatus.FINISHED);
        assertThat(holder.getDiscounts()).isEqualTo(discounts);
    }
}
