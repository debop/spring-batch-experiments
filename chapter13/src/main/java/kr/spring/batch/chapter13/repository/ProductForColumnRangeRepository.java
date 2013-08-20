package kr.spring.batch.chapter13.repository;

import kr.spring.batch.chapter13.domain.ProductForColumnRange;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * kr.spring.batch.chapter13.repository.ProductForColumnRangeRepository
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 20. 오후 5:19
 */
public interface ProductForColumnRangeRepository extends JpaRepository<ProductForColumnRange, Long> {
}
