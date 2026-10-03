package poc.paymentservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import poc.paymentservice.events.InventoryEvent;
import poc.paymentservice.events.PaymentEvent;
import poc.paymentservice.models.Payment;
import poc.paymentservice.services.PaymentService;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class InventoryFailedListener {

    private final PaymentService paymentService;
    private final PaymentEventProducer paymentEventProducer;

    public InventoryFailedListener(PaymentService paymentService, PaymentEventProducer paymentEventProducer) {
        this.paymentService = paymentService;
        this.paymentEventProducer = paymentEventProducer;
    }

    @KafkaListener(
            topics = "inventory-events",
            groupId = "payment-compensation-service",
            properties = {
                    "spring.json.value.default.type=poc.paymentservice.events.InventoryEvent",
                    "spring.json.use.type.headers=false"
            }
    )
    public void handle(InventoryEvent event) {

        if (!"INVENTORY_FAILED".equals(event.eventType())) {
            return;
        }

        System.out.println("[SAGA " + event.sagaId() + "] INVENTORY_FAILED recibido por Payment");
        Payment payment = paymentService.refundByOrderId(event.orderId());
        System.out.println("[SAGA " + event.sagaId() + "] PAYMENT_REFUNDED");

        PaymentEvent refundEvent = new PaymentEvent(
            UUID.randomUUID(),
            event.sagaId(),
            event.orderId(),
            "PAYMENT_REFUNDED",
            event.productId(),
            event.quantity(),
            payment.getAmount(),
            event.shippingAddress(),
            OffsetDateTime.now()
        );

        paymentEventProducer.publish(refundEvent);
    }
}
