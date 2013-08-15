package kr.spring.batch.chapter09.test.besteffort;

import kr.spring.batch.chapter09.domain.Inventory;
import kr.spring.batch.chapter09.domain.Order;
import kr.spring.batch.chapter09.domain.OrderItem;
import kr.spring.batch.chapter09.repository.InventoryRepository;
import kr.spring.batch.chapter09.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.Message;
import javax.persistence.EntityManagerFactory;
import java.util.Arrays;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.spring.batch.chapter09.test.besteffort.DetectDuplicateTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 15. 오후 10:17
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DetectDuplicateConfiguration.class })
public class DetectDuplicateTest {

	@Autowired
	EntityManagerFactory emf;

	@Autowired
	InventoryRepository inventoryRepository;

	@Autowired
	OrderRepository orderRepository;

	@Before
	public void setUp() throws Exception {

		orderRepository.deleteAll();
		inventoryRepository.deleteAll();
		orderRepository.flush();

		for (int i = 1; i < 15; i++) {
//			Order order = new Order(String.valueOf(i), false);
//			orderRepository.save(order);

			Inventory inventory = new Inventory(String.valueOf(i), 50);
			inventoryRepository.save(inventory);
		}
		orderRepository.flush();
	}


	@Autowired JobLauncher jobLauncher;

	@Autowired Job updateInventoryJobWithMockReader;

	@Autowired Job updateInventoryJob;

	@Autowired JmsTemplate jmsTemplate;

	@Autowired QueueViewMBean orderQueueView;

	@Autowired ItemReader<Message> mockReader;

	@Test
	public void noDuplicate() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder().addLong("date", System.currentTimeMillis()).toJobParameters();
		prepareQueue();

		assertThat(orderQueueView.getQueueSize()).isEqualTo(13);
		JobExecution exec = jobLauncher.run(updateInventoryJob, jobParameters);
		assertThat(exec.getStatus()).isEqualTo(BatchStatus.COMPLETED);
		assertInventoryOk();
		assertThat(orderQueueView.getQueueSize()).isEqualTo(0);
	}


	private void prepareQueue() {
		emptyQueue();
		fillInQueue();
	}

	private void emptyQueue() {
		while (jmsTemplate.receive() != null) { }
	}

	private void fillInQueue() {
		jmsTemplate.convertAndSend(createOrder("1", createItem("1"), createItem("2", 2)));
		jmsTemplate.convertAndSend(createOrder("2", createItem("1", 2)));

		for (int i = 3; i <= 13; i++) {
			String id = String.valueOf(i);
			jmsTemplate.convertAndSend(createOrder(id, createItem(id)));
		}
	}


	private void assertInventoryOk() {
		int initialCount = 50;
		assertThat(getProductQuantity("1")).isEqualTo(initialCount - 3);
		assertThat(getProductQuantity("2")).isEqualTo(initialCount - 2);

		for (int i = 3; i <= 13; i++) {
			assertThat(getProductQuantity(String.valueOf(i))).isEqualTo(initialCount - 1);
		}
	}

	private Integer getProductQuantity(String productId) {
		Inventory inventory = inventoryRepository.findByProductId(productId);
		return inventory.getQuantity();
	}

	private Order createOrder(String orderId, OrderItem... items) {
		Order order = new Order(orderId, false);
		order.getItems().addAll(Arrays.asList(items));

		for (OrderItem item : order.getItems()) {
			item.setOrder(order);
		}
		return order;
	}

	private OrderItem createItem(String productId, Integer quantity) {
		return new OrderItem(productId, quantity);
	}

	private OrderItem createItem(String productId) {
		return createItem(productId, 1);
	}
}
