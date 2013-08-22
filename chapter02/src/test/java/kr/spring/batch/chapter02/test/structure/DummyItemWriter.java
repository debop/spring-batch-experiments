package kr.spring.batch.chapter02.test.structure;

import kr.spring.batch.chapter02.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * kr.spring.batch.chapter02.test.structure.DummyItemWriter
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오전 9:24
 */
@Slf4j
public class DummyItemWriter implements ItemWriter<Product> {

    public List<Product> products = new ArrayList<Product>();

    @Override
    public void write(List<? extends Product> items) throws Exception {
        products.addAll(items);
    }
}
