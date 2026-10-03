package poc.paymentservice.events;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record OrderCreatedEvent (
    UUID eventId,
    UUID sagaId,
    Long orderId,
    String customerName,
    Long productId,
    Integer quantity,
    BigDecimal totalAmount,
    String shippingAddress,
    OffsetDateTime occurredAt
) {}
