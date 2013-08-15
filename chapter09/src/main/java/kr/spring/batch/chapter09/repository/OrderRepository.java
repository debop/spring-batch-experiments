package kr.spring.batch.chapter09.repository;

import kr.spring.batch.chapter09.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * kr.spring.batch.chapter09.repository.OrderRepository
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 15. 오전 10:09
 */
public interface OrderRepository extends JpaRepository<Order, String> {
}
