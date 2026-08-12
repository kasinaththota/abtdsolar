package com.abtd.solarbackend.inventory.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateInventoryRequest {

    @NotNull(message = "Product Id is required")
    private Long productId;

    @NotNull(message = "Available quantity is required")
    @PositiveOrZero(message = "Available quantity cannot be negative")
    private Integer availableQuantity;

    @NotNull(message = "Reserved quantity is required")
    @PositiveOrZero(message = "Reserved quantity cannot be negative")
    private Integer reservedQuantity;

    @NotNull(message = "Minimum stock is required")
    @PositiveOrZero(message = "Minimum stock cannot be negative")
    private Integer minimumStock;

    @NotNull(message = "Maximum stock is required")
    @Positive(message = "Maximum stock must be greater than zero")
    private Integer maximumStock;

    @Size(max = 100)
    private String warehouseLocation;
}