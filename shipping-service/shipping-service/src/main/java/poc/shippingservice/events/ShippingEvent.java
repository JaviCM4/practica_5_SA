package poc.shippingservice.events;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ShippingEvent (
    UUID eventId,
    UUID sagaId,
    Long orderId,
    String eventType,
    OffsetDateTime occurredAt
) {}
