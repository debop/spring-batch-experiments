package kr.spring.batch.chapter05.test;

import com.google.common.collect.Lists;
import kr.spring.batch.chapter05.Product;
import lombok.Getter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * kr.spring.batch.chapter05.test.DummyProductItemWriter
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 1. 오후 2:02
 */
@Component
public class DummyProductItemWriter implements ItemWriter<Product> {

	@Getter
	private List<Product> products = Lists.newArrayList();

	public void write(List<? extends Product> items) throws Exception {
		products.addAll(items);
	}
}
