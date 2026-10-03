package poc.orderservice.dtos;

import java.math.BigDecimal;

public record PaymentInfo(
    Long id,
    Long orderId,
    BigDecimal amount,
    String status,
    String message
) {}