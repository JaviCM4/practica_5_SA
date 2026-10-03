package poc.orderservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import poc.orderservice.events.OrderCreatedEvent;

@Component
public class OrderEventProducer {

    private static final String ORDER_EVENTS_TOPIC = "order-events";
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public OrderEventProducer(KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrderCreated(OrderCreatedEvent event) {

        kafkaTemplate.send(
                ORDER_EVENTS_TOPIC,
                event.sagaId().toString(),
                event
        );

        System.out.println("[SAGA " + event.sagaId() + "] ORDER_CREATED publicado");
    }
}
