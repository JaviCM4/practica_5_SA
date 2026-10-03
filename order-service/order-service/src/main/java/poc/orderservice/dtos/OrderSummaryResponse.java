package poc.orderservice.dtos;

import java.util.UUID;

public record OrderSummaryResponse(
    Long orderId,
    UUID sagaId,
    String orderStatus,
    PaymentInfo payment,
    InventoryInfo inventory,
    ShippingInfo shipping
) {}