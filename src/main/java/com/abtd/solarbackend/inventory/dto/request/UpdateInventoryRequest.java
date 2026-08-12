package com.abtd.solarbackend.inventory.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateInventoryRequest {

    @NotNull
    @PositiveOrZero
    private Integer availableQuantity;

    @NotNull
    @PositiveOrZero
    private Integer reservedQuantity;

    @NotNull
    @PositiveOrZero
    private Integer minimumStock;

    @NotNull
    @Positive
    private Integer maximumStock;

    @Size(max = 100)
    private String warehouseLocation;
}