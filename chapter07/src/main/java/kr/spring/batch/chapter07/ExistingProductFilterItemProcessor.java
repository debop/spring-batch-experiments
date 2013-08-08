package kr.spring.batch.chapter07;

import kr.spring.batch.chapter07.jpa.ProductRepository;
import lombok.Setter;
import org.springframework.batch.item.ItemProcessor;

/**
 * kr.spring.batch.chapter07.ExistingProductFilterItemProcessor
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 8. 오후 5:30
 */
public class ExistingProductFilterItemProcessor implements ItemProcessor<Product, Product> {

    @Setter ProductRepository repository;

    @Override
    public Product process(Product product) throws Exception {
        // 기존 Product가 존재하면 필터링 해버린다.
        return needsToBeFiltered(product) ? null : product;
    }

    private boolean needsToBeFiltered(Product item) {
        return repository.exists(item.getId());
    }
}
