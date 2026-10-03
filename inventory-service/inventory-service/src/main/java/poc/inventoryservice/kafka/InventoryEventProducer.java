package poc.inventoryservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import poc.inventoryservice.events.InventoryEvent;

@Component
public class InventoryEventProducer {

    private static final String TOPIC = "inventory-events";

    private final KafkaTemplate<String, InventoryEvent> kafkaTemplate;

    public InventoryEventProducer(KafkaTemplate<String, InventoryEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(InventoryEvent event) {

        kafkaTemplate.send(
            TOPIC,
            event.sagaId().toString(),
            event
        );

        System.out.println("[SAGA " + event.sagaId() + "] " + event.eventType() + " publicado");
    }
}
