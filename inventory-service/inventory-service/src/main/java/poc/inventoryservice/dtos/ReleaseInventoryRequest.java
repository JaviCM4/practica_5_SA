package poc.inventoryservice.dtos;

import jakarta.validation.constraints.NotNull;

public record ReleaseInventoryRequest (
    @NotNull
    Long orderId
) {}
