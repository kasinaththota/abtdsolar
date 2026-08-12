package com.abtd.solarbackend.inventory.controller;

import com.abtd.solarbackend.common.dto.PageRequestDto;
import com.abtd.solarbackend.common.dto.PageResponse;
import com.abtd.solarbackend.common.response.ApiResponse;
import com.abtd.solarbackend.common.response.ResponseBuilder;
import com.abtd.solarbackend.common.constants.InventoryMessages;
import com.abtd.solarbackend.enums.InventoryStatus;
import com.abtd.solarbackend.inventory.dto.request.CreateInventoryRequest;
import com.abtd.solarbackend.inventory.dto.request.UpdateInventoryRequest;
import com.abtd.solarbackend.inventory.dto.response.InventoryResponse;
import com.abtd.solarbackend.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','INVENTORY')")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    @Operation(summary = "Create Inventory")
    public ResponseEntity<ApiResponse<InventoryResponse>> createInventory(
            @Valid @RequestBody CreateInventoryRequest request) {

        InventoryResponse response = inventoryService.createInventory(request);

        return ResponseBuilder.created(
                InventoryMessages.INVENTORY_CREATED,
                response
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Inventory By Id")
    public ResponseEntity<ApiResponse<InventoryResponse>> getInventoryById(
            @PathVariable Long id) {

        return ResponseBuilder.ok(
                InventoryMessages.INVENTORY_FETCHED,
                inventoryService.getInventoryById(id)
        );
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get Inventory By Product Id")
    public ResponseEntity<ApiResponse<InventoryResponse>> getInventoryByProductId(
            @PathVariable Long productId) {

        return ResponseBuilder.ok(
                InventoryMessages.INVENTORY_FETCHED,
                inventoryService.getInventoryByProductId(productId)
        );
    }

    @GetMapping
    @Operation(summary = "Get All Inventories")
    public ResponseEntity<ApiResponse<PageResponse<InventoryResponse>>> getAllInventories(
            @ModelAttribute PageRequestDto pageRequest) {

        return ResponseBuilder.ok(
                InventoryMessages.INVENTORIES_FETCHED,
                inventoryService.getAllInventories(pageRequest)
        );
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get Inventory By Status")
    public ResponseEntity<ApiResponse<PageResponse<InventoryResponse>>> getInventoriesByStatus(
            @PathVariable InventoryStatus status,
            @ModelAttribute PageRequestDto pageRequest) {

        return ResponseBuilder.ok(
                InventoryMessages.INVENTORIES_FETCHED,
                inventoryService.getInventoriesByStatus(status, pageRequest)
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Inventory")
    public ResponseEntity<ApiResponse<InventoryResponse>> updateInventory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateInventoryRequest request) {

        InventoryResponse response =
                inventoryService.updateInventory(id, request);

        return ResponseBuilder.ok(
                InventoryMessages.INVENTORY_UPDATED,
                response
        );
    }
}