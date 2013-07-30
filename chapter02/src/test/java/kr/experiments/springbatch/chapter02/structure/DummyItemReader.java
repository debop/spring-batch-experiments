package kr.experiments.springbatch.chapter02.structure;

import kr.experiments.springbatch.chapter02.domain.Product;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * kr.experiments.springbatch.chapter02.structure.DummyItemReader
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오전 12:14
 */
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
