package kr.spring.batch.chapter08.retry;

import lombok.Setter;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.List;

/**
 * kr.spring.batch.chapter08.retry.DiscountsTasklet
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 10. 오후 5:16
 */
public class DiscountsTasklet implements Tasklet {

    @Setter private DiscountService discountService;
    @Setter private DiscountsHolder discountsHolder;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<Discount> discounts = discountService.getDiscounts();
        discountsHolder.setDiscounts(discounts);
        return RepeatStatus.FINISHED;
    }
}
