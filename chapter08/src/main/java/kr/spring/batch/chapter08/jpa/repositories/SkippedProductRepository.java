package kr.spring.batch.chapter08.jpa.repositories;

import kr.spring.batch.chapter08.SkippedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * kr.spring.batch.chapter08.jpa.repositories.SkippedProductRepository
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 9. 오후 3:10
 */
public interface SkippedProductRepository extends JpaRepository<SkippedProduct, Long> {
}
