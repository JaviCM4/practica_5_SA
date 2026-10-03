package poc.inventoryservice.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import poc.inventoryservice.dtos.ReleaseInventoryRequest;
import poc.inventoryservice.models.InventoryReservation;
import poc.inventoryservice.services.InventoryService;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/release")
    public InventoryReservation release(@Valid @RequestBody ReleaseInventoryRequest request) {

        return inventoryService.release(
                request.orderId()
        );
    }

    @GetMapping("/order/{orderId}")
    public InventoryReservation findByOrder(@PathVariable Long orderId) {
        return inventoryService.findByOrderId(orderId);
    }
}
