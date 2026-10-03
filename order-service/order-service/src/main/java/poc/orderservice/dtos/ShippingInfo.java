package poc.orderservice.dtos;

public record ShippingInfo(
    Long id,
    Long orderId,
    String shippingAddress,
    String status,
    String message
) {}
