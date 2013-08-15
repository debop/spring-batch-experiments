package kr.spring.batch.chapter09.batch;

import kr.spring.batch.chapter09.domain.Order;
import kr.spring.batch.chapter09.repository.OrderRepository;
import lombok.Setter;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * kr.spring.batch.chapter09.batch.ShippedOrderWriter
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 15. 오전 9:50
 */
public class ShippedOrderWriter implements ItemWriter<Order> {

	@Setter OrderRepository orderRepository;

	@Override
	public void write(List<? extends Order> orders) throws Exception {
		for (Order order : orders) {
			order.setShipped(true);
			orderRepository.save(order);
		}
		orderRepository.flush();
	}
}
