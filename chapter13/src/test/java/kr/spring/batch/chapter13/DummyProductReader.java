package kr.spring.batch.chapter13;

import kr.spring.batch.chapter13.domain.Product;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * kr.spring.batch.chapter13.DummyProductReader
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 20. 오후 2:01
 */
public class DummyProductReader implements ItemReader<Product> {

    private AtomicInteger count = new AtomicInteger(0);
    private Integer max = 100;

    public DummyProductReader() {}

    public DummyProductReader(Integer max) {
        this.max = max;
    }

    @Override
    public synchronized Product read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (count.incrementAndGet() <= max) {
            Product product = new Product(String.valueOf(count.get()));
            ThreadUtils.writeThreadExecutionMessage("read", product);
            return product;
        } else {
            return null;
        }
    }
}
