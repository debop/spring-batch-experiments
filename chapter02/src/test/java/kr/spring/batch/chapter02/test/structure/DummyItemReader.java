package kr.spring.batch.chapter02.test.structure;

import kr.spring.batch.chapter02.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * kr.spring.batch.chapter02.test.structure.DummyItemReader
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오전 12:14
 */
@Slf4j
public class DummyItemReader implements ItemReader<Product> {

    // HINT: 자료 구조의 초기화 시에 이렇게 한다.
    Queue<Product> products = new LinkedBlockingQueue<Product>() {{
        add(new Product("1"));
        add(new Product("2"));
        add(new Product("3"));
        add(new Product("4"));
    }};

    @Override
    public Product read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return products.poll();
    }
}
