package poc.orderservice.services;

import org.springframework.stereotype.Service;
import poc.orderservice.client.InventoryClient;
import poc.orderservice.client.PaymentClient;
import poc.orderservice.client.ShippingClient;
import poc.orderservice.dtos.OrderSummaryResponse;
import poc.orderservice.dtos.PaymentInfo;
import poc.orderservice.dtos.InventoryInfo;
import poc.orderservice.dtos.ShippingInfo;
import poc.orderservice.models.Order;
import poc.orderservice.repositories.OrderRepository;

@Service
public class OrderSummaryService {

    private final OrderRepository orderRepository;
    private final PaymentClient paymentClient;
    private final InventoryClient inventoryClient;
    private final ShippingClient shippingClient;

    public OrderSummaryService(OrderRepository orderRepository, PaymentClient paymentClient, InventoryClient inventoryClient, ShippingClient shippingClient) {
        this.orderRepository = orderRepository;
        this.paymentClient = paymentClient;
        this.inventoryClient = inventoryClient;
        this.shippingClient = shippingClient;
    }

    public OrderSummaryResponse getSummary(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada: " + orderId));

        PaymentInfo payment = paymentClient.getPayment(orderId);
        InventoryInfo inventory = inventoryClient.getInventory(orderId);
        ShippingInfo shipping = shippingClient.getShipping(orderId);

        return new OrderSummaryResponse(
                order.getId(),
                order.getSagaId(),
                order.getStatus().name(),
                payment,
                inventory,
                shipping
        );
    }
}
