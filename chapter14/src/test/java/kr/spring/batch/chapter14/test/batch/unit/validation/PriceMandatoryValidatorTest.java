package kr.spring.batch.chapter14.test.batch.unit.validation;

import kr.spring.batch.chapter14.batch.validation.PriceMandatoryValidator;
import kr.spring.batch.chapter14.domain.Product;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.validator.ValidationException;

import java.math.BigDecimal;

/**
 * kr.spring.batch.chapter14.test.batch.unit.validation.PriceMandatoryValidatorTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 19. 오전 10:13
 */
public class PriceMandatoryValidatorTest {

    private PriceMandatoryValidator validator;
    private Product product;

    @Before
    public void before() {
        validator = new PriceMandatoryValidator();
        product = new Product();
    }

    @Test
    public void testValidProduct() {
        product.setPrice(new BigDecimal("100.0"));
        validator.validate(product);
    }

    @Test(expected = ValidationException.class)
    public void testInvalidProduct() {
        // price 값을 설정하지 않는다면 예외가 발생해야 합니다.
        validator.validate(product);
    }
}
