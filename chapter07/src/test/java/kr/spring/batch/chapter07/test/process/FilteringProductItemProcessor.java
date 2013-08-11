package kr.spring.batch.chapter07.test.process;

import kr.spring.batch.chapter07.Product;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.batch.item.ItemProcessor;

/**
 * kr.spring.batch.chapter07.test.process.FilteringProductItemProcessor
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 8. 오후 3:33
 */
public class FilteringProductItemProcessor implements ItemProcessor<Product, Product> {
	@Override
	public Product process(Product item) throws Exception {
		return needsToBeFiltered(item) ? null : item;
	}

	private boolean needsToBeFiltered(Product item) {
		String id = item.getId();
		String lastDigit = id.substring(id.length() - 1, id.length());

		return NumberUtils.isDigits(lastDigit) && NumberUtils.toInt(lastDigit) % 2 == 1;
	}
}
