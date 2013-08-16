package kr.spring.batch.chapter09.test.besteffort;

import kr.spring.batch.chapter09.domain.Inventory;
import kr.spring.batch.chapter09.domain.OrderEntity;
import kr.spring.batch.chapter09.repository.InventoryRepository;
import kr.spring.batch.chapter09.repository.OrderEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.fest.assertions.Assertions.assertThat;

/**
 * 중복 작업이 안되도록 하는
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 16. 오후 1:19
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { IdempotencyConfiguration.class })
public class IdempotencyTest {

    @PersistenceContext
    EntityManager em;

    @Autowired JobLauncher jobLauncher;
    @Autowired Job job;
    @Autowired JmsTemplate jmsTemplate;

    @Autowired QueueViewMBean shippedOrderQueueView;

    @Autowired OrderEntityRepository orderRepository;
    @Autowired InventoryRepository inventoryRepository;

    @Test
    public void idempotency() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder().addLong("date", System.currentTimeMillis())
                                                                .toJobParameters();

        prepareQueue();
        Assertions.assertThat(shippedOrderQueueView.getQueueSize()).isEqualTo(14);
        assertOrderState(false);

        JobExecution exec = jobLauncher.run(job, jobParameters);
        assertThat(exec.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertOrderState(true);
        assertThat(shippedOrderQueueView.getQueueSize()).isEqualTo(0);

        jobParameters = new JobParametersBuilder().addLong("date", System.currentTimeMillis()).toJobParameters();
        prepareQueue();
        assertThat(shippedOrderQueueView.getQueueSize()).isEqualTo(14);
        jobLauncher.run(job, jobParameters);
        assertThat(exec.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertOrderState(true);
        assertThat(shippedOrderQueueView.getQueueSize()).isEqualTo(0);
    }


    private void prepareQueue() {
        emptyQueue();
        fillInQueue();
    }

    private void emptyQueue() {
        while (jmsTemplate.receive() != null) {}
    }

    private void fillInQueue() {
        for (int i = 1; i <= 14; i++) {
            jmsTemplate.convertAndSend(new OrderEntity(String.valueOf(i), false));
        }
    }

    private void assertOrderState(boolean shipped) {
        Long countShipped = orderRepository.countByShipped(shipped);
        Long total = orderRepository.count();
        assertThat(countShipped).isEqualTo(total);
    }

    private int getProductQuantity(String productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId);
        return (inventory != null) ? inventory.getQuantity() : 0;
    }

}
