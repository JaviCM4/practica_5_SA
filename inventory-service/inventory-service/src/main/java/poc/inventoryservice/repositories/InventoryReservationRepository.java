package poc.inventoryservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poc.inventoryservice.models.InventoryReservation;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventoryReservationRepository extends JpaRepository<InventoryReservation, Integer> {

    Optional<InventoryReservation> findByOrderId(Long orderId);

    Optional<InventoryReservation> findBySagaId(UUID sagaId);
}
