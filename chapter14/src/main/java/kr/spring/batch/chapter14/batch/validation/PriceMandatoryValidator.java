package kr.spring.batch.chapter14.batch.validation;

import kr.spring.batch.chapter14.domain.Product;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

/**
 * kr.spring.batch.chapter14.batch.validation.PriceMandatoryValidator
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 19. 오전 9:34
 */
public class PriceMandatoryValidator implements Validator<Product> {
    @Override
    public void validate(Product value) throws ValidationException {
        if (value.getPrice() == null) {
            throw new ValidationException("제품가는 필수입니다.");
        }
    }
}

