package kr.spring.batch.chapter06.jpa;

import kr.spring.batch.chapter06.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * kr.spring.batch.chapter06.jpa.ProductRepository
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 7. 오후 2:45
 */
@Transactional
public interface ProductRepository extends JpaRepository<Product, String> {

}
