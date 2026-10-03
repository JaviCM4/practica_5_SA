package poc.inventoryservice.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poc.inventoryservice.enums.InventoryStatus;
import poc.inventoryservice.events.PaymentEvent;
import poc.inventoryservice.models.InventoryReservation;
import poc.inventoryservice.models.InventoryStock;
import poc.inventoryservice.repositories.InventoryReservationRepository;
import poc.inventoryservice.repositories.InventoryStockRepository;

@Service
public class InventoryService {

    private final InventoryStockRepository stockRepository;
    private final InventoryReservationRepository reservationRepository;

    public InventoryService(InventoryStockRepository stockRepository, InventoryReservationRepository reservationRepository) {
        this.stockRepository = stockRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public boolean reserve(PaymentEvent event) {

        InventoryStock stock = stockRepository
                .findByProductIdForUpdate(event.productId())
                .orElse(null);

        // Producto inexistente.
        if (stock == null) {
            saveFailedReservation(event);
            return false;
        }

        // No existe suficiente stock.
        if (stock.getAvailableQuantity() < event.quantity()) {
            saveFailedReservation(event);
            return false;
        }

        // Descontamos stock.
        stock.setAvailableQuantity(stock.getAvailableQuantity() - event.quantity());

        stockRepository.save(stock);

        // Guardamos la reserva.
        InventoryReservation reservation = new InventoryReservation();
        reservation.setOrderId(event.orderId());
        reservation.setSagaId(event.sagaId());
        reservation.setProductId(event.productId());
        reservation.setQuantity(event.quantity());
        reservation.setStatus(InventoryStatus.RESERVED);

        reservationRepository.save(reservation);

        return true;
    }

    @Transactional
    public InventoryReservation release(Long orderId) {

        InventoryReservation reservation = reservationRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada para la orden: " + orderId));

        if (reservation.getStatus() == InventoryStatus.RELEASED) {
            return reservation;
        }

        if (reservation.getStatus() != InventoryStatus.RESERVED) {
            throw new IllegalStateException("No se puede liberar inventario en estado " + reservation.getStatus());
        }

        InventoryStock stock = stockRepository.findByProductIdForUpdate(reservation.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + reservation.getProductId()));

        // Devolvemos el stock.
        stock.setAvailableQuantity(stock.getAvailableQuantity() + reservation.getQuantity());
        stockRepository.save(stock);

        // Marcamos la reserva como compensada.
        reservation.setStatus(InventoryStatus.RELEASED);
        return reservationRepository.save(reservation);
    }

    public InventoryReservation findByOrderId(Long orderId) {

        return reservationRepository.findByOrderId(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada para orden: " + orderId));
    }

    private void saveFailedReservation(PaymentEvent event) {
        InventoryReservation reservation = new InventoryReservation();
        reservation.setOrderId(event.orderId());
        reservation.setSagaId(event.sagaId());
        reservation.setProductId(event.productId());
        reservation.setQuantity(event.quantity());
        reservation.setStatus(InventoryStatus.FAILED);
        reservationRepository.save(reservation);
    }
}
