package poc.orderservice.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import poc.orderservice.dtos.PaymentInfo;

@Component
public class PaymentClient {

    private final RestClient restClient;

    public PaymentClient(RestClient.Builder builder) {

        this.restClient = builder
                .baseUrl("http://localhost:8082")
                .build();
    }

    @CircuitBreaker(
            name = "paymentService",
            fallbackMethod = "paymentFallback"
    )
    @Retry(
            name = "paymentService",
            fallbackMethod = "paymentFallback"
    )
    public PaymentInfo getPayment(Long orderId) {

        return restClient.get()
                .uri("/payments/order/{orderId}", orderId)
                .retrieve()
                .body(PaymentInfo.class);
    }

    public PaymentInfo paymentFallback(Long orderId, Throwable throwable) {

        return new PaymentInfo(
                null,
                orderId,
                null,
                "UNAVAILABLE",
                "Payment Service no está disponible temporalmente"
        );
    }
}
