package poc.orderservice.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import poc.orderservice.dtos.InventoryInfo;

@Component
public class InventoryClient {

    private final RestClient restClient;

    public InventoryClient(RestClient.Builder builder) {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8083")
                .build();
    }

    @CircuitBreaker(
            name = "inventoryService",
            fallbackMethod = "inventoryFallback"
    )
    @Retry(
            name = "paymentService",
            fallbackMethod = "paymentFallback"
    )
    public InventoryInfo getInventory(Long orderId) {

        return restClient.get()
                .uri("/inventory/order/{orderId}", orderId)
                .retrieve()
                .body(InventoryInfo.class);
    }

    public InventoryInfo inventoryFallback(Long orderId, Throwable throwable) {

        return new InventoryInfo(
                null,
                orderId,
                null,
                null,
                "UNAVAILABLE",
                "Inventory Service no está disponible temporalmente"
        );
    }
}
