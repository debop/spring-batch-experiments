package kr.spring.batch.chapter07.validation;

import kr.spring.batch.chapter07.Product;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

import java.math.BigDecimal;

/**
 * kr.spring.batch.chapter07.validation.ProductValidator
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 7. 오후 6:07
 */
public class ProductValidator implements Validator<Product> {
	@Override
	public void validate(Product product) throws ValidationException {
		if (BigDecimal.ZERO.compareTo(product.getPrice()) >= 0) {
			throw new ValidationException("제품 가격은 양수여야 합니다.");
		}
	}
}
