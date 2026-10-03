package poc.orderservice.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import poc.orderservice.dtos.CreateOrderRequest;
import poc.orderservice.dtos.OrderSummaryResponse;
import poc.orderservice.models.Order;
import poc.orderservice.services.OrderService;
import poc.orderservice.services.OrderSummaryService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderSummaryService orderSummaryService;

    public OrderController(OrderService orderService, OrderSummaryService orderSummaryService) {
        this.orderService = orderService;
        this.orderSummaryService = orderSummaryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order create(@Valid @RequestBody CreateOrderRequest request) {
        return orderService.create(request);
    }

    @DeleteMapping("/{id}")
    public Order cancel(@PathVariable Long id) {
        return orderService.cancelOrder(id);
    }

    @GetMapping("/{id}/summary")
    public OrderSummaryResponse summary(@PathVariable Long id) {
        return orderSummaryService.getSummary(id);
    }
}
