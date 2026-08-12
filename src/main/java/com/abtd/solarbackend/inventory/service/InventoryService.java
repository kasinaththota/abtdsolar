package com.abtd.solarbackend.inventory.service;

import com.abtd.solarbackend.common.dto.PageRequestDto;
import com.abtd.solarbackend.common.dto.PageResponse;
import com.abtd.solarbackend.enums.InventoryStatus;
import com.abtd.solarbackend.inventory.dto.request.CreateInventoryRequest;
import com.abtd.solarbackend.inventory.dto.request.UpdateInventoryRequest;
import com.abtd.solarbackend.inventory.dto.response.InventoryResponse;

public interface InventoryService {

    InventoryResponse createInventory(CreateInventoryRequest request);

    InventoryResponse getInventoryById(Long id);

    InventoryResponse getInventoryByProductId(Long productId);

    PageResponse<InventoryResponse> getAllInventories(PageRequestDto pageRequest);

    PageResponse<InventoryResponse> getInventoriesByStatus(
            InventoryStatus status,
            PageRequestDto pageRequest);

    InventoryResponse updateInventory(
            Long id,
            UpdateInventoryRequest request);
}