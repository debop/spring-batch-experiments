package kr.spring.batch.chapter14.test.batch.unit.validation;

import kr.spring.batch.chapter14.batch.validation.PositivePriceValidator;
import kr.spring.batch.chapter14.domain.Product;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.validator.ValidationException;

import java.math.BigDecimal;

/**
 * kr.spring.batch.chapter14.test.batch.unit.validation.PositivePriceValidatorTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 19. 오전 10:05
 */
public class PositivePriceValidatorTest {

    private PositivePriceValidator validator;
    private Product product;

    @Before
    public void before() {
        validator = new PositivePriceValidator();
        product = new Product();
    }

    @Test
    public void testPositivePrice() {
        product.setPrice(new BigDecimal("100.0"));
        validator.validate(product);
    }

    @Test(expected = ValidationException.class)
    public void testZeroPrice() {
        product.setPrice(new BigDecimal("0.0"));
        validator.validate(product);
    }

    @Test(expected = ValidationException.class)
    public void testNegativePrice() {
        product.setPrice(new BigDecimal("-100.0"));
        validator.validate(product);
    }
}
