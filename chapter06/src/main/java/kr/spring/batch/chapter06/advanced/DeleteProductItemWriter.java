package kr.spring.batch.chapter06.advanced;

import kr.spring.batch.chapter06.Product;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * kr.spring.batch.chapter06.advanced.DeleteProductItemWriter
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 7. 오후 5:05
 */
@Transactional
public class DeleteProductItemWriter implements ItemWriter<Product> {

    @Autowired
    ProductRepository repository;

    @Override
    public void write(List<? extends Product> items) throws Exception {
        for (Product item : items)
            repository.delete(item.getId());

        repository.flush();
    }
}
