package poc.shippingservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import poc.shippingservice.events.InventoryEvent;
import poc.shippingservice.events.ShippingEvent;
import poc.shippingservice.services.ShippingService;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class InventoryEventListener {

    private final ShippingService shippingService;
    private final ShippingEventProducer shippingEventProducer;

    public InventoryEventListener(ShippingService shippingService, ShippingEventProducer shippingEventProducer) {
        this.shippingService = shippingService;
        this.shippingEventProducer = shippingEventProducer;
    }

    @KafkaListener(
        topics = "inventory-events",
        groupId = "shipping-service"
    )
    public void handle(InventoryEvent event) {

        if (!"INVENTORY_RESERVED".equals(event.eventType())) {
            return;
        }

        System.out.println("[SAGA " + event.sagaId() + "] INVENTORY_RESERVED recibido");
        shippingService.schedule(event);

        ShippingEvent shippingEvent = new ShippingEvent(
            UUID.randomUUID(),
            event.sagaId(),
            event.orderId(),
            "SHIPPING_SCHEDULED",
            OffsetDateTime.now()
        );

        shippingEventProducer.publish(shippingEvent);
    }
}
