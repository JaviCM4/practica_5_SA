package poc.orderservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import poc.orderservice.events.InventoryEvent;
import poc.orderservice.services.OrderService;

@Component
public class InventoryFailedListener {

    private final OrderService orderService;

    public InventoryFailedListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(
            topics = "inventory-events",
            groupId = "order-compensation-service",
            properties = {
                    "spring.json.value.default.type=poc.orderservice.events.InventoryEvent",
                    "spring.json.use.type.headers=false"
            }
    )
    public void handle(InventoryEvent event) {

        if (!"INVENTORY_FAILED".equals(event.eventType())) {
            return;
        }

        System.out.println("[SAGA " + event.sagaId() + "] INVENTORY_FAILED recibido por Order");
        orderService.cancelOrder(event.orderId());
        System.out.println("[SAGA " + event.sagaId() + "] ORDER_CANCELLED");
    }
}
