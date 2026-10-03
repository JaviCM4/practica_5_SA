package poc.orderservice.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import poc.orderservice.dtos.ShippingInfo;

@Component
public class ShippingClient {

    private final RestClient restClient;

    public ShippingClient(RestClient.Builder builder) {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8084")
                .build();
    }

    @CircuitBreaker(
            name = "shippingService",
            fallbackMethod = "shippingFallback"
    )
    @Retry(
            name = "paymentService",
            fallbackMethod = "paymentFallback"
    )
    public ShippingInfo getShipping(Long orderId) {

        return restClient.get()
                .uri("/shipping/order/{orderId}", orderId)
                .retrieve()
                .body(ShippingInfo.class);
    }

    public ShippingInfo shippingFallback(
            Long orderId,
            Throwable throwable
    ) {

        return new ShippingInfo(
                null,
                orderId,
                null,
                "UNAVAILABLE",
                "Shipping Service no está disponible temporalmente"
        );
    }
}
