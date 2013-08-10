package kr.spring.batch.chapter08.retry;

import java.util.List;

/**
 * kr.spring.batch.chapter08.retry.DiscountService
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 10. 오후 5:14
 */
public interface DiscountService {

    List<Discount> getDiscounts();
}
