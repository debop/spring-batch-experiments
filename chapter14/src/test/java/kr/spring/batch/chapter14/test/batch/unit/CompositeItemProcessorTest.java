package kr.spring.batch.chapter14.test.batch.unit;

import kr.spring.batch.chapter14.batch.validation.PositivePriceValidator;
import kr.spring.batch.chapter14.batch.validation.PriceMandatoryValidator;
import kr.spring.batch.chapter14.domain.Product;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.ValidatingItemProcessor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.spring.batch.chapter14.test.batch.unit.CompositeItemProcessorTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 19. 오전 10:35
 */
public class CompositeItemProcessorTest {

    private CompositeItemProcessor<Product, Product> processor;

    @Before
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void setUp() {
        List delegates = new ArrayList();

        ValidatingItemProcessor processor1 = new ValidatingItemProcessor();
        processor1.setFilter(true);
        processor1.setValidator(new PriceMandatoryValidator());
        delegates.add(processor1);

        ValidatingItemProcessor processor2 = new ValidatingItemProcessor();
        processor2.setFilter(true);
        processor2.setValidator(new PositivePriceValidator());
        delegates.add(processor2);

        processor = new CompositeItemProcessor<Product, Product>();
        processor.setDelegates(delegates);
    }

    @Test
    public void processor() throws Exception {
        Product p1 = new Product();
        p1.setPrice(new BigDecimal(100.0f));
        Product p2 = processor.process(p1);
        assertThat(p2).isNotNull();
    }

    @Test
    public void processorFail() throws Exception {
        Product p1 = new Product();
        p1.setPrice(new BigDecimal(-100.0f));
        Product p2 = processor.process(p1);
        assertThat(p2).isNull();
    }
}
