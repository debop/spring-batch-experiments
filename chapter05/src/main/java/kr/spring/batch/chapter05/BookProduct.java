package kr.spring.batch.chapter05;

import lombok.Getter;
import lombok.Setter;

/**
 * kr.spring.batch.chapter05.BookProduct
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 1. 오후 1:38
 */
@Getter
@Setter
public class BookProduct extends Product {

    private String publisher;

    private static final long serialVersionUID = -3928693529337055437L;
}
