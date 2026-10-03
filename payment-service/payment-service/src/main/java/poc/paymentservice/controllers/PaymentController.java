package poc.paymentservice.controllers;

import org.springframework.web.bind.annotation.*;
import poc.paymentservice.models.Payment;
import poc.paymentservice.services.PaymentService;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/{id}/refund")
    public Payment refund(@PathVariable Long id) {
        return paymentService.refund(id);
    }

    @GetMapping("/order/{orderId}")
    public Payment findByOrder(@PathVariable Long orderId) {
        return paymentService.findByOrderId(orderId);
    }
}
