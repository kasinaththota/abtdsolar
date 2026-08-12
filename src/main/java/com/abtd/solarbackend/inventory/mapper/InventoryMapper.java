package com.abtd.solarbackend.inventory.mapper;

import com.abtd.solarbackend.inventory.dto.request.CreateInventoryRequest;
import com.abtd.solarbackend.inventory.dto.request.UpdateInventoryRequest;
import com.abtd.solarbackend.inventory.dto.response.InventoryResponse;
import com.abtd.solarbackend.inventory.entity.Inventory;
import com.abtd.solarbackend.product.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {

    /**
     * Create Inventory Entity
     */
    public Inventory toEntity(CreateInventoryRequest request, Product product) {

        return Inventory.builder()
                .product(product)
                .availableQuantity(request.getAvailableQuantity())
                .reservedQuantity(request.getReservedQuantity())
                .minimumStock(request.getMinimumStock())
                .maximumStock(request.getMaximumStock())
                .warehouseLocation(request.getWarehouseLocation())
                .build();
    }

    /**
     * Entity -> Response
     */
    public InventoryResponse toResponse(Inventory inventory) {

        Product product = inventory.getProduct();

        return InventoryResponse.builder()
                .id(inventory.getId())
                .productId(product.getId())
                .productCode(product.getProductCode())
                .productName(product.getName())
                .availableQuantity(inventory.getAvailableQuantity())
                .reservedQuantity(inventory.getReservedQuantity())
                .freeQuantity(inventory.getFreeQuantity())
                .minimumStock(inventory.getMinimumStock())
                .maximumStock(inventory.getMaximumStock())
                .warehouseLocation(inventory.getWarehouseLocation())
                .status(inventory.getStatus())
                .createdAt(inventory.getCreatedAt())
                .updatedAt(inventory.getUpdatedAt())
                .build();
    }

    /**
     * Update Existing Inventory
     */
    public void updateEntity(UpdateInventoryRequest request,
                             Inventory inventory) {

        inventory.setAvailableQuantity(request.getAvailableQuantity());
        inventory.setReservedQuantity(request.getReservedQuantity());
        inventory.setMinimumStock(request.getMinimumStock());
        inventory.setMaximumStock(request.getMaximumStock());
        inventory.setWarehouseLocation(request.getWarehouseLocation());
    }
}