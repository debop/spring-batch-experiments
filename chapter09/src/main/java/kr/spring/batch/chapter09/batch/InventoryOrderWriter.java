package kr.spring.batch.chapter09.batch;

import kr.spring.batch.chapter09.domain.Inventory;
import kr.spring.batch.chapter09.domain.InventoryOrder;
import kr.spring.batch.chapter09.domain.OrderEntity;
import kr.spring.batch.chapter09.domain.OrderItem;
import kr.spring.batch.chapter09.repository.InventoryOrderRepository;
import kr.spring.batch.chapter09.repository.InventoryRepository;
import kr.spring.batch.chapter09.repository.OrderEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * kr.spring.batch.chapter09.batch.InventoryOrderWriter
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 15. 오전 9:50
 */
@Slf4j
public class InventoryOrderWriter implements ItemWriter<OrderEntity> {

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    InventoryOrderRepository inventoryOrderRepository;

    @Autowired
    OrderEntityRepository orderRepository;

    @Override
    public void write(List<? extends OrderEntity> orders) throws Exception {
        orderRepository.save(orders);
        for (OrderEntity order : orders) {
            log.info("write orderEntity... order=[{}]", order);
            updateInventory(order);
            track(order);
        }
        inventoryRepository.flush();
    }

    private void updateInventory(OrderEntity order) {
        for (OrderItem item : order.getItems()) {
            Inventory inventory = inventoryRepository.findByProductId(item.getProductId());
            inventory.decreaseQuantity(item.getQuantity());
            inventoryRepository.save(inventory);
        }
    }

    private void track(OrderEntity order) {
        InventoryOrder inventoryOrder = new InventoryOrder(order, new Date());
        inventoryOrderRepository.save(inventoryOrder);
    }
}
