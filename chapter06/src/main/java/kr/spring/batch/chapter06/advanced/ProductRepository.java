package kr.spring.batch.chapter06.advanced;

import kr.spring.batch.chapter06.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * kr.spring.batch.chapter06.advanced.ProductRepository
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 7. 오후 5:03
 */
public interface ProductRepository extends JpaRepository<Product, String> {
}
