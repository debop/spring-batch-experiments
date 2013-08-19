package kr.spring.batch.chapter13.repository;

import kr.spring.batch.chapter13.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * kr.spring.batch.chapter13.repository.ProductRepository
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 19. 오후 11:32
 */
public interface ProductRepository extends JpaRepository<Product, String> {
}
