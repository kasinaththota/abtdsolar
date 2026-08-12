package com.abtd.solarbackend.inventory.dto.response;

import com.abtd.solarbackend.enums.InventoryStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryResponse {

    private Long id;

    private Long productId;

    private String productCode;

    private String productName;

    private Integer availableQuantity;

    private Integer reservedQuantity;

    private Integer freeQuantity;

    private Integer minimumStock;

    private Integer maximumStock;

    private String warehouseLocation;

    private InventoryStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}