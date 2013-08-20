package kr.spring.batch.chapter13;

import kr.spring.batch.chapter13.domain.Product;
import org.springframework.batch.item.ItemWriter;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * kr.spring.batch.chapter13.DummyProductWriter
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 20. 오후 2:04
 */
public class DummyProductWriter implements ItemWriter<Product> {

    private CopyOnWriteArrayList<Product> products = new CopyOnWriteArrayList<Product>();

    @Override
    public void write(List<? extends Product> items) throws Exception {
        ThreadUtils.writeThreadExecutionMessage("write", items);
        for (Product product : items)
            processProduct(product);
    }

    private void processProduct(Product product) throws InterruptedException {
        Thread.sleep(5);
        products.add(product);
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public void clear() {
        products.clear();
    }
}
