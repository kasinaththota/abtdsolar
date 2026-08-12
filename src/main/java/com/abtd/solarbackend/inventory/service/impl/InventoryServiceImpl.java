package com.abtd.solarbackend.inventory.service.impl;

import com.abtd.solarbackend.common.dto.PageRequestDto;
import com.abtd.solarbackend.common.dto.PageResponse;
import com.abtd.solarbackend.common.response.PageResponseBuilder;
import com.abtd.solarbackend.common.constants.InventoryMessages;
import com.abtd.solarbackend.enums.InventoryStatus;
import com.abtd.solarbackend.inventory.dto.request.CreateInventoryRequest;
import com.abtd.solarbackend.inventory.dto.request.UpdateInventoryRequest;
import com.abtd.solarbackend.inventory.dto.response.InventoryResponse;
import com.abtd.solarbackend.inventory.entity.Inventory;
import com.abtd.solarbackend.inventory.exception.DuplicateInventoryException;
import com.abtd.solarbackend.inventory.exception.InvalidInventoryException;
import com.abtd.solarbackend.inventory.exception.InventoryNotFoundException;
import com.abtd.solarbackend.inventory.mapper.InventoryMapper;
import com.abtd.solarbackend.inventory.repository.InventoryRepository;
import com.abtd.solarbackend.inventory.service.InventoryService;
import com.abtd.solarbackend.product.entity.Product;
import com.abtd.solarbackend.product.exception.ProductNotFoundException;
import com.abtd.solarbackend.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    private final ProductRepository productRepository;

    private final InventoryMapper inventoryMapper;

    @Override
    public InventoryResponse createInventory(CreateInventoryRequest request) {

        Product product = findProduct(request.getProductId());

        if (inventoryRepository.existsByProductId(product.getId())) {
            throw new DuplicateInventoryException(
                    InventoryMessages.INVENTORY_ALREADY_EXISTS);
        }

        validateInventory(
                request.getAvailableQuantity(),
                request.getReservedQuantity(),
                request.getMinimumStock(),
                request.getMaximumStock());

        Inventory inventory = inventoryMapper.toEntity(request, product);

        inventory = inventoryRepository.save(inventory);

        return inventoryMapper.toResponse(inventory);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getInventoryById(Long id) {

        return inventoryMapper.toResponse(findInventory(id));
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getInventoryByProductId(Long productId) {

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException(
                        "Inventory not found for Product Id : " + productId));

        return inventoryMapper.toResponse(inventory);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<InventoryResponse> getAllInventories(PageRequestDto pageRequest) {

        Pageable pageable = pageRequest.toPageable();

        Page<InventoryResponse> response = inventoryRepository
                .findAll(pageable)
                .map(inventoryMapper::toResponse);

        return PageResponseBuilder.build(response);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<InventoryResponse> getInventoriesByStatus(
            InventoryStatus status,
            PageRequestDto pageRequest) {

        Pageable pageable = pageRequest.toPageable();

        Page<InventoryResponse> response = inventoryRepository
                .findByStatus(status, pageable)
                .map(inventoryMapper::toResponse);

        return PageResponseBuilder.build(response);
    }

    @Override
    public InventoryResponse updateInventory(
            Long id,
            UpdateInventoryRequest request) {

        Inventory inventory = findInventory(id);

        validateInventory(
                request.getAvailableQuantity(),
                request.getReservedQuantity(),
                request.getMinimumStock(),
                request.getMaximumStock());

        inventoryMapper.updateEntity(request, inventory);

        inventory = inventoryRepository.save(inventory);

        return inventoryMapper.toResponse(inventory);
    }

    /**
     * Find Inventory by Id
     */
    private Inventory findInventory(Long id) {

        return inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException(id));
    }

    /**
     * Find Product by Id
     */
    private Product findProduct(Long productId) {

        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    /**
     * Inventory Business Validations
     */
    private void validateInventory(
            Integer availableQuantity,
            Integer reservedQuantity,
            Integer minimumStock,
            Integer maximumStock) {

        if (reservedQuantity > availableQuantity) {
            throw new InvalidInventoryException(
                    InventoryMessages.INVALID_RESERVED_QUANTITY);
        }

        if (minimumStock > maximumStock) {
            throw new InvalidInventoryException(
                    InventoryMessages.INVALID_STOCK_LIMITS);
        }
    }
}