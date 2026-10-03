package poc.paymentservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import poc.paymentservice.events.PaymentEvent;

@Component
public class PaymentEventProducer {

    private static final String PAYMENT_EVENTS_TOPIC = "payment-events";

    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public PaymentEventProducer(KafkaTemplate<String, PaymentEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(PaymentEvent event) {

        kafkaTemplate.send(
                PAYMENT_EVENTS_TOPIC,
                event.sagaId().toString(),
                event
        );

        System.out.println("[SAGA " + event.sagaId() + "] " + event.eventType() + " publicado");
    }
}
