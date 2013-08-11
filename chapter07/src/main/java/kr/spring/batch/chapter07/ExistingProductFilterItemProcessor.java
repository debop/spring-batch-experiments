package kr.spring.batch.chapter07;

import kr.spring.batch.chapter07.jpa.ProductRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * kr.spring.batch.chapter07.ExistingProductFilterItemProcessor
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 8. 오후 5:30
 */
public class ExistingProductFilterItemProcessor implements ItemProcessor<Product, Product> {

	@Autowired
	ProductRepository repository;

	@Override
	public Product process(Product product) throws Exception {
		// 기존 Product가 존재하면 필터링 해버린다.
		return needsToBeFiltered(product) ? null : product;
	}

	private boolean needsToBeFiltered(Product product) {
		return repository.exists(product.getId());
	}
}
