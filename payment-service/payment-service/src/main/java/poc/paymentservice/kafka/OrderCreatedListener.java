package poc.paymentservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import poc.paymentservice.events.OrderCreatedEvent;
import poc.paymentservice.events.PaymentEvent;
import poc.paymentservice.services.PaymentService;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class OrderCreatedListener {

    private final PaymentService paymentService;
    private final PaymentEventProducer paymentEventProducer;

    public OrderCreatedListener(PaymentService paymentService, PaymentEventProducer paymentEventProducer) {
        this.paymentService = paymentService;
        this.paymentEventProducer = paymentEventProducer;
    }

    @KafkaListener(
            topics = "order-events",
            groupId = "payment-service"
    )
    public void handle(OrderCreatedEvent event) {

        System.out.println("[SAGA " + event.sagaId() + "] ORDER_CREATED recibido");
        paymentService.processPayment(event);

        PaymentEvent paymentEvent = new PaymentEvent(
            UUID.randomUUID(),
            event.sagaId(),
            event.orderId(),
            "PAYMENT_COMPLETED",
            event.productId(),
            event.quantity(),
            event.totalAmount(),
            event.shippingAddress(),
            OffsetDateTime.now()
        );

        paymentEventProducer.publish(paymentEvent);
    }
}
