package poc.inventoryservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import poc.inventoryservice.events.InventoryEvent;
import poc.inventoryservice.events.PaymentEvent;
import poc.inventoryservice.services.InventoryService;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class PaymentEventListener {

    private final InventoryService inventoryService;
    private final InventoryEventProducer inventoryEventProducer;

    public PaymentEventListener(InventoryService inventoryService, InventoryEventProducer inventoryEventProducer) {
        this.inventoryService = inventoryService;
        this.inventoryEventProducer = inventoryEventProducer;
    }

    @KafkaListener(
        topics = "payment-events",
        groupId = "inventory-service"
    )
    public void handle(PaymentEvent event) {

        if (!"PAYMENT_COMPLETED".equals(event.eventType())) {
            return;
        }

        System.out.println("[SAGA " + event.sagaId() + "] PAYMENT_COMPLETED recibido");
        boolean reserved = inventoryService.reserve(event);

        String eventType = reserved
            ? "INVENTORY_RESERVED"
            : "INVENTORY_FAILED";

        InventoryEvent inventoryEvent = new InventoryEvent(
            UUID.randomUUID(),
            event.sagaId(),
            event.orderId(),
            eventType,
            event.productId(),
            event.quantity(),
            event.shippingAddress(),
            OffsetDateTime.now()
        );

        inventoryEventProducer.publish(inventoryEvent);
    }
}
