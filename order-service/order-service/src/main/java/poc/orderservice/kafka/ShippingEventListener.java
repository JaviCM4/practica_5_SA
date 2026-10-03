package poc.orderservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import poc.orderservice.events.ShippingEvent;
import poc.orderservice.services.OrderService;

@Component
public class ShippingEventListener {

    private final OrderService orderService;

    public ShippingEventListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(
            topics = "shipping-events",
            groupId = "order-service"
    )
    public void handle(ShippingEvent event) {

        if (!"SHIPPING_SCHEDULED".equals(event.eventType())) {
            return;
        }

        System.out.println("[SAGA " + event.sagaId() + "] SHIPPING_SCHEDULED recibido");
        orderService.completeOrder(event.orderId());
        System.out.println("[SAGA " + event.sagaId() + "] ORDER_COMPLETED");
    }
}
