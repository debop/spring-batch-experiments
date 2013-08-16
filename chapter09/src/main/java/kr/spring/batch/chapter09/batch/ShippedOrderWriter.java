package kr.spring.batch.chapter09.batch;

import kr.spring.batch.chapter09.domain.OrderEntity;
import kr.spring.batch.chapter09.repository.OrderEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * kr.spring.batch.chapter09.batch.ShippedOrderWriter
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 15. 오전 9:50
 */
@Slf4j
public class ShippedOrderWriter implements ItemWriter<OrderEntity> {

    @Autowired
    OrderEntityRepository orderEntityRepository;

    @Override
    public void write(List<? extends OrderEntity> orders) throws Exception {
        for (OrderEntity order : orders) {
            log.debug("OrderEntity is shipped... OrderId=[{}]", order.getOrderId());
            order.setShipped(true);
            orderEntityRepository.save(order);
        }
        orderEntityRepository.flush();
    }
}
