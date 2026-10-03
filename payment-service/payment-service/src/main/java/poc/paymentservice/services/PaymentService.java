package poc.paymentservice.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poc.paymentservice.enums.PaymentStatus;
import poc.paymentservice.events.OrderCreatedEvent;
import poc.paymentservice.models.Payment;
import poc.paymentservice.repositories.PaymentRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public Payment processPayment(OrderCreatedEvent event) {
        Payment payment = new Payment();
        payment.setOrderId(event.orderId());
        payment.setSagaId(event.sagaId());
        payment.setAmount(event.totalAmount());
        payment.setStatus(PaymentStatus.COMPLETED);
        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment refundByOrderId(Long orderId) {

        Payment payment = paymentRepository
                .findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado para la orden: " + orderId));

        if (payment.getStatus() == PaymentStatus.REFUNDED) {
            return payment;
        }

        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new IllegalStateException("El pago no puede ser reembolsado. Estado actual: " + payment.getStatus());
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment refund(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado: " + paymentId));

        if (payment.getStatus() == PaymentStatus.REFUNDED) {
            return payment;
        }

        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new IllegalStateException("No se puede reembolsar un pago en estado " + payment.getStatus());
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        return paymentRepository.save(payment);
    }

    public Payment findByOrderId(Long orderId) {

        return paymentRepository.findByOrderId(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado para orden: " + orderId));
    }
}
