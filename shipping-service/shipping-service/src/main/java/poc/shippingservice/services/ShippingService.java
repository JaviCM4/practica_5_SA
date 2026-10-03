package poc.shippingservice.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poc.shippingservice.enums.ShippingStatus;
import poc.shippingservice.events.InventoryEvent;
import poc.shippingservice.models.Shipment;
import poc.shippingservice.repositories.ShipmentRepository;

@Service
public class ShippingService {

    private final ShipmentRepository shipmentRepository;

    public ShippingService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @Transactional
    public Shipment schedule(InventoryEvent event) {
        Shipment shipment = new Shipment();
        shipment.setOrderId(event.orderId());
        shipment.setSagaId(event.sagaId());
        shipment.setShippingAddress(event.shippingAddress());
        shipment.setStatus(ShippingStatus.SCHEDULED);
        return shipmentRepository.save(shipment);
    }

    @Transactional
    public Shipment cancel(Long shipmentId) {

        Shipment shipment = shipmentRepository
                .findById(shipmentId)
                .orElseThrow(() -> new IllegalArgumentException("Envío no encontrado: " + shipmentId));

        if (shipment.getStatus() == ShippingStatus.CANCELLED) {
            return shipment;
        }

        shipment.setStatus(ShippingStatus.CANCELLED);
        return shipmentRepository.save(shipment);
    }

    public Shipment findByOrderId(Long orderId) {

        return shipmentRepository.findByOrderId(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Envío no encontrado para orden: " + orderId));
    }
}
