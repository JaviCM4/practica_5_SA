package poc.orderservice.dtos;

public record InventoryInfo(
    Long id,
    Long orderId,
    Long productId,
    Integer quantity,
    String status,
    String message
) {}
