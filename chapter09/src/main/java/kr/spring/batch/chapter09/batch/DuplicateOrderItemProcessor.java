package kr.spring.batch.chapter09.batch;

import kr.spring.batch.chapter09.domain.Order;
import kr.spring.batch.chapter09.repository.OrderRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.support.converter.MessageConversionException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

/**
 * kr.spring.batch.chapter09.batch.DuplicateOrderItemProcessor
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 15. 오전 9:50
 */
public class DuplicateOrderItemProcessor implements ItemProcessor<Message, Order> {

	@Autowired
	OrderRepository orderRepository;

	@Override
	public Order process(Message message) throws Exception {
		Order order = extractOrder(message);
		if (order != null) {
			if (message.getJMSRedelivered()) {
				if (orderAlreadyProcessed(order)) {
					order = null;
				}
			}
		}
		return order;
	}

	private Order extractOrder(Message message) {
		if (message instanceof ObjectMessage) {
			try {
				return (Order) ((ObjectMessage) message).getObject();
			} catch (JMSException e) {
				throw new MessageConversionException("couldn't extract order", e);
			}
		}
		return null;
	}

	private boolean orderAlreadyProcessed(Order order) {
		return order != null &&
				orderRepository.findByOrderId(order.getOrderId()) != null;
	}
}
