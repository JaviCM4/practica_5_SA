package poc.orderservice.events;

import java.time.OffsetDateTime;
import java.util.UUID;

public record InventoryEvent (
    UUID eventId,
    UUID sagaId,
    Long orderId,
    String eventType,
    Long productId,
    Integer quantity,
    String shippingAddress,
    OffsetDateTime occurredAt
) {}
