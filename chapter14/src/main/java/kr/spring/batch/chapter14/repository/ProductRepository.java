package kr.spring.batch.chapter14.repository;

import kr.spring.batch.chapter14.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * kr.spring.batch.chapter14.repository.ProductRepository
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 18. 오후 9:32
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
}
