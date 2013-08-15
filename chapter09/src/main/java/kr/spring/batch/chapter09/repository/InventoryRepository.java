package kr.spring.batch.chapter09.repository;

import kr.spring.batch.chapter09.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * kr.spring.batch.chapter09.repository.InventoryRepository
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 15. 오전 10:32
 */
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

	Inventory findByProductId(String productId);
}
