package poc.shippingservice.controllers;

import org.springframework.web.bind.annotation.*;
import poc.shippingservice.models.Shipment;
import poc.shippingservice.services.ShippingService;

@RestController
@RequestMapping("/shipping")
public class ShippingController {

    private final ShippingService shippingService;

    public ShippingController(ShippingService shippingService) {
        this.shippingService = shippingService;
    }

    @DeleteMapping("/{id}")
    public Shipment cancel(@PathVariable Long id) {
        return shippingService.cancel(id);
    }

    @GetMapping("/order/{orderId}")
    public Shipment findByOrder(@PathVariable Long orderId) {
        return shippingService.findByOrderId(orderId);
    }
}
