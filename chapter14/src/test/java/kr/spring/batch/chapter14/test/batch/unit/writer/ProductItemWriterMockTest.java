package kr.spring.batch.chapter14.test.batch.unit.writer;

import kr.spring.batch.chapter14.batch.ProductItemWriter;
import kr.spring.batch.chapter14.domain.Product;
import kr.spring.batch.chapter14.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * kr.spring.batch.chapter14.test.batch.unit.writer.ProductItemWriterMockTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 19. 오전 10:16
 */
public class ProductItemWriterMockTest {

    private Product p;
    private ProductRepository productRepository;
    private ProductItemWriter writer = new ProductItemWriter();
    private List<Product> items;

    @Before
    public void setUp() {
        p = new Product();
        p.setId("211");
        p.setName("BlackBerry");
        items = new ArrayList<Product>();
        items.add(p);

        writer = new ProductItemWriter();
        productRepository = mock(ProductRepository.class);
        writer.setProductRepository(productRepository);
    }

    @Test
    public void testWriteProduct() throws Exception {
        //when(productRepository.save(anyListOf(Product.class))).thenReturn(anyListOf(Product.class));
        writer.write(items);

        verify(productRepository, times(1)).save(anyListOf(Product.class));
        verify(productRepository, times(1)).flush();
        verifyNoMoreInteractions(productRepository);
    }
}
