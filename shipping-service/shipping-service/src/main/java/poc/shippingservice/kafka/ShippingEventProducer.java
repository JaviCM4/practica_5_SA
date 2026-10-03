package poc.shippingservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import poc.shippingservice.events.ShippingEvent;

@Component
public class ShippingEventProducer {

    private static final String TOPIC = "shipping-events";

    private final KafkaTemplate<String, ShippingEvent> kafkaTemplate;

    public ShippingEventProducer(KafkaTemplate<String, ShippingEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(ShippingEvent event) {

        kafkaTemplate.send(
            TOPIC,
            event.sagaId().toString(),
            event
        );
        System.out.println("[SAGA " + event.sagaId() + "] " + event.eventType() + " publicado");
    }
}
