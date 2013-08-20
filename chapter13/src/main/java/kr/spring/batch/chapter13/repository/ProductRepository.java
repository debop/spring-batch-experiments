package kr.spring.batch.chapter13.repository;

import kr.spring.batch.chapter13.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * kr.spring.batch.chapter13.repository.ProductRepository
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 19. 오후 11:32
 */
public interface ProductRepository extends JpaRepository<Product, String> {

    @Query("select count(p.id) from Product p where p.processed = false")
    long countByNotProcessed();

    @Query("select p from Product p where p.processed = false")
    List<Product> findByNotProcessed();
}
