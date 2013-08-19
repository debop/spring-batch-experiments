package kr.spring.batch.chapter14.batch.validation;

import kr.spring.batch.chapter14.domain.Product;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

import java.math.BigDecimal;

/**
 * kr.spring.batch.chapter14.batch.validation.PositivePriceValidator
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 19. 오전 9:33
 */
public class PositivePriceValidator implements Validator<Product> {
    @Override
    public void validate(Product value) throws ValidationException {
        if (BigDecimal.ZERO.compareTo(value.getPrice()) >= 0) {
            throw new ValidationException("제품가는 양수여야 합니다.");
        }
    }
}
