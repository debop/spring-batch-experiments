package kr.spring.batch.chapter08;

import org.springframework.batch.item.ItemWriter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * kr.spring.batch.chapter08.ProductJpaItemWriter
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 9. 오후 4:28
 */
public class ProductJpaItemWriter implements ItemWriter<Product> {

    @PersistenceContext
    EntityManager em;

    @Override
    public void write(List<? extends Product> items) throws Exception {
        for (Product item : items) {
            if (item != null)
                em.persist(item);
        }
        em.flush();
    }
}
