package kr.spring.batch.chapter13;

import kr.spring.batch.chapter13.domain.ProductForColumnRange;
import org.springframework.batch.item.ItemWriter;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * kr.spring.batch.chapter13.DummyProductForColumnRangeWriter
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 20. 오후 2:28
 */
public class DummyProductForColumnRangeWriter implements ItemWriter<ProductForColumnRange> {

    private CopyOnWriteArrayList<ProductForColumnRange> products =
        new CopyOnWriteArrayList<ProductForColumnRange>();

    @Override
    public void write(List<? extends ProductForColumnRange> items) throws Exception {
        ThreadUtils.writeThreadExecutionMessage("write", items);
        for (ProductForColumnRange product : items) {
            processProduct(product);
        }
    }

    private void processProduct(ProductForColumnRange product) throws InterruptedException {
        Thread.sleep(5);
        products.add(product);
    }

    public List<ProductForColumnRange> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public void clear() {
        products.clear();
    }
}
