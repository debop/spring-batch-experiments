package kr.spring.batch.chapter07;

import kr.spring.batch.chapter07.jpa.ProductRepository;
import lombok.Setter;
import org.springframework.batch.item.ItemProcessor;

/**
 * kr.spring.batch.chapter07.IdToProductItemProcessor
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 8. 오후 2:49
 */
public class IdToProductItemProcessor implements ItemProcessor<String, Product> {

	@Setter private ProductRepository productRepository;

	@Override
	public Product process(String productId) throws Exception {
		return productRepository.findOne(productId);
	}
}
