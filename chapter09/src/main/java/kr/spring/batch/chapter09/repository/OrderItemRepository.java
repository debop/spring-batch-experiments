package kr.spring.batch.chapter09.repository;

import kr.spring.batch.chapter09.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * kr.spring.batch.chapter09.repository.OrderItemRepository
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 15. 오전 10:14
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
}
