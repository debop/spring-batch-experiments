package kr.spring.batch.chapter14.test.batch.unit.listener;

import kr.spring.batch.chapter14.batch.ProductItemListener;
import kr.spring.batch.chapter14.domain.Product;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.file.FlatFileItemWriter;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * kr.spring.batch.chapter14.test.batch.unit.listener.ProductItemListenerTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 18. 오후 11:14
 */
public class ProductItemListenerTest {

	private Product p = null;
	private FlatFileItemWriter<Product> writer = null;
	private List<Product> products = null;

	@Before
	public void setUp() {
		p = new Product();
		p.setId("211");
		p.setName("BlackBerry");
		products = Arrays.asList(p);
		writer = mock(FlatFileItemWriter.class);
	}

	@Test
	public void afterProcess() throws Exception {
		ProductItemListener listener = new ProductItemListener();
		listener.setExcludeWriter(writer);
		listener.afterProcess(p, null);

		verify(writer, times(1)).write(products);
	}

	@Test
	public void afterProcessResult() throws Exception {
		ProductItemListener listener = new ProductItemListener();
		listener.setExcludeWriter(writer);
		listener.afterProcess(p, p);

		verify(writer, never()).write(products);
	}

}
