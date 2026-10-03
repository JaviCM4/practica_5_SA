package poc.orderservice.services;

import poc.orderservice.enums.OrderStatus;
import poc.orderservice.events.OrderCreatedEvent;
import poc.orderservice.models.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poc.orderservice.kafka.OrderEventProducer;
import poc.orderservice.repositories.OrderRepository;
import poc.orderservice.dtos.CreateOrderRequest;

import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventProducer eventProducer;

    public OrderService(OrderRepository orderRepository, OrderEventProducer eventProducer) {
        this.orderRepository = orderRepository;
        this.eventProducer = eventProducer;
    }

    @Transactional
    public Order create(CreateOrderRequest dto) {

        UUID sagaId = UUID.randomUUID();
        Order order = dto.createEntity(sagaId);
        order = orderRepository.save(order);

        OrderCreatedEvent event = order.createdEvent();
        eventProducer.publishOrderCreated(event);
        return order;
    }

    @Transactional
    public void completeOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada: " + orderId));

        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
    }

    @Transactional
    public Order cancelOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada: " + orderId));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            return order;
        }

        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }
}
