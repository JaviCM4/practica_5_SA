package poc.orderservice.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import poc.orderservice.enums.OrderStatus;
import poc.orderservice.models.Order;

import java.math.BigDecimal;
import java.util.UUID;

@Value
public class CreateOrderRequest {

    @NotBlank
    String customerName;

    @NotNull
    Long productId;

    @NotNull
    @Min(1)
    Integer quantity;

    @NotNull
    @DecimalMin("0.01")
    BigDecimal totalAmount;

    @NotBlank
    String shippingAddress;

    public Order createEntity(UUID sagaId) {
        Order order = new Order();
        order.setSagaId(sagaId);
        order.setCustomerName(customerName);
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setTotalAmount(totalAmount);
        order.setShippingAddress(shippingAddress);
        order.setStatus(OrderStatus.PENDING);
        return order;
    }
}
