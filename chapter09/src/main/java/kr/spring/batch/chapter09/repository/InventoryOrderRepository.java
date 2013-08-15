package kr.spring.batch.chapter09.repository;

import kr.spring.batch.chapter09.domain.InventoryOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * kr.spring.batch.chapter09.repository.InventoryOrderRepository
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 15. 오전 10:50
 */
public interface InventoryOrderRepository extends JpaRepository<InventoryOrder, Long> {
}
