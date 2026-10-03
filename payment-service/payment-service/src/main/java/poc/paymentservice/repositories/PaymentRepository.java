package poc.paymentservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import poc.paymentservice.models.Payment;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(Long orderId);

    Optional<Payment> findBySagaId(UUID sagaId);
}
