package kr.spring.batch.chapter09.repository;

import kr.spring.batch.chapter09.domain.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * kr.spring.batch.chapter09.repository.OrderEntityRepository
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 15. 오전 10:09
 */
public interface OrderEntityRepository extends JpaRepository<OrderEntity, String> {

    OrderEntity findByOrderId(String orderId);

    List<OrderEntity> findByShipped(Boolean shipped);

    @Query("select count(o) from OrderEntity o where o.shipped=:shipped")
    Long countByShipped(@Param("shipped") Boolean shipped);
}
