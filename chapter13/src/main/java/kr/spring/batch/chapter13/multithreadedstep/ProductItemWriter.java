package kr.spring.batch.chapter13.multithreadedstep;

import kr.spring.batch.chapter13.ThreadUtils;
import kr.spring.batch.chapter13.domain.Product;
import kr.spring.batch.chapter13.repository.ProductRepository;
import lombok.Setter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * kr.spring.batch.chapter13.multithreadedstep.ProductItemWriter
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 19. 오후 11:32
 */
@Transactional
public class ProductItemWriter implements ItemWriter<Product> {

    @Setter
    ProductRepository productRepository;

    @Override
    public void write(List<? extends Product> items) throws Exception {
        ThreadUtils.writeThreadExecutionMessage("write", items);
        for (Product item : items) {
            item.setProcessed(true);
            productRepository.save(item);
        }
        productRepository.flush();
    }
}
