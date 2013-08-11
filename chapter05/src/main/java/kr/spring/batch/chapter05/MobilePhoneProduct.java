package kr.spring.batch.chapter05;

import lombok.Getter;
import lombok.Setter;

/**
 * kr.spring.batch.chapter05.MobilePhoneProduct
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 1. 오후 1:39
 */
@Getter
@Setter
public class MobilePhoneProduct extends Product {

	private String manufacturer;

	private static final long serialVersionUID = 501496932942144935L;
}
