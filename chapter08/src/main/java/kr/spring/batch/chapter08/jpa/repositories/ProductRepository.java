package kr.spring.batch.chapter08.jpa.repositories;

import kr.spring.batch.chapter08.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * kr.spring.batch.chapter08.jpa.repositories.ProductRepository
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 9. 오후 4:47
 */
public interface ProductRepository extends JpaRepository<Product, String> {


}
