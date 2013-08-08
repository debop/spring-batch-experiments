package kr.spring.batch.chapter07.jpa;

import kr.spring.batch.chapter07.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * kr.spring.batch.chapter07.jpa.ProductRepository
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 8. 오후 2:46
 */
public interface ProductRepository extends JpaRepository<Product, String> {
}
