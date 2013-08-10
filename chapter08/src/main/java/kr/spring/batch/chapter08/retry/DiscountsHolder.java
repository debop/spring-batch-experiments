package kr.spring.batch.chapter08.retry;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * kr.spring.batch.chapter08.retry.DiscountsHolder
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 10. 오후 5:15
 */
public class DiscountsHolder {

    @Getter
    @Setter
    private List<Discount> discounts;


}
