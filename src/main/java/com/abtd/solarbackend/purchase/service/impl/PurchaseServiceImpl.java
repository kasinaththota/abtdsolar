package com.abtd.solarbackend.purchase.service.impl;

import com.abtd.solarbackend.common.dto.PageRequestDto;
import com.abtd.solarbackend.common.dto.PageResponse;
import com.abtd.solarbackend.common.response.PageResponseBuilder;
import com.abtd.solarbackend.enums.InventoryStatus;
import com.abtd.solarbackend.enums.PurchaseStatus;
import com.abtd.solarbackend.inventory.entity.Inventory;
import com.abtd.solarbackend.inventory.exception.InventoryNotFoundException;
import com.abtd.solarbackend.inventory.repository.InventoryRepository;
import com.abtd.solarbackend.product.entity.Product;
import com.abtd.solarbackend.product.exception.ProductNotFoundException;
import com.abtd.solarbackend.product.repository.ProductRepository;
import com.abtd.solarbackend.purchase.dto.request.CreatePurchaseRequest;
import com.abtd.solarbackend.purchase.dto.request.PurchaseItemRequest;
import com.abtd.solarbackend.purchase.dto.request.UpdatePurchaseRequest;
import com.abtd.solarbackend.purchase.dto.response.PurchaseResponse;
import com.abtd.solarbackend.purchase.entity.Purchase;
import com.abtd.solarbackend.purchase.entity.PurchaseItem;
import com.abtd.solarbackend.purchase.exception.InvalidPurchaseException;
import com.abtd.solarbackend.purchase.exception.PurchaseNotFoundException;
import com.abtd.solarbackend.purchase.mapper.PurchaseMapper;
import com.abtd.solarbackend.purchase.repository.PurchaseRepository;
import com.abtd.solarbackend.purchase.service.PurchaseService;
import com.abtd.solarbackend.vendor.entity.Vendor;
import com.abtd.solarbackend.vendor.exception.VendorNotFoundException;
import com.abtd.solarbackend.vendor.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final VendorRepository vendorRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final PurchaseMapper purchaseMapper;

    /**
     * Find Purchase
     */
    private Purchase findPurchase(Long id) {

        return purchaseRepository.findById(id)
                .orElseThrow(() ->
                        new PurchaseNotFoundException(id));
    }

    /**
     * Find Vendor
     */
    private Vendor findVendor(Long id) {

        return vendorRepository.findById(id)
                .orElseThrow(() ->
                        new VendorNotFoundException(id));
    }

    /**
     * Find Product
     */
    private Product findProduct(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(id));
    }

    /**
     * Generate Purchase Number
     */
    private String generatePurchaseNumber() {

        Optional<Purchase> latest =
                purchaseRepository.findTopByOrderByIdDesc();

        if (latest.isEmpty()) {
            return "PUR000001";
        }

        String lastNumber = latest.get().getPurchaseNumber();

        int next = Integer.parseInt(lastNumber.substring(3)) + 1;

        return String.format("PUR%06d", next);
    }

    /**
     * Calculate GST Amount
     */
    private BigDecimal calculateGst(BigDecimal amount,
                                    BigDecimal gstPercentage) {

        return amount.multiply(gstPercentage)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    /**
     * Update Inventory Status
     */
    private void updateInventoryStatus(Inventory inventory) {

        if (inventory.getAvailableQuantity() == null ||
                inventory.getAvailableQuantity() <= 0) {

            inventory.setStatus(InventoryStatus.OUT_OF_STOCK);

        } else if (inventory.getAvailableQuantity()
                .compareTo(inventory.getMinimumStock()) <= 0) {

            inventory.setStatus(InventoryStatus.LOW_STOCK);

        } else {

            inventory.setStatus(InventoryStatus.AVAILABLE);
        }
    }

    @Override
    public PurchaseResponse createPurchase(CreatePurchaseRequest request) {

        Vendor vendor = findVendor(request.getVendorId());

        Purchase purchase = purchaseMapper.toEntity(request, vendor);

        purchase.setPurchaseNumber(generatePurchaseNumber());
        purchase.setStatus(PurchaseStatus.ORDERED);

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalGst = BigDecimal.ZERO;

        for (PurchaseItemRequest itemRequest : request.getItems()) {

            Product product = findProduct(itemRequest.getProductId());

            if (itemRequest.getQuantity() == null || itemRequest.getQuantity() <= 0) {
                throw new InvalidPurchaseException(
                        "Quantity must be greater than zero.");
            }

            if (itemRequest.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new InvalidPurchaseException(
                        "Unit price must be greater than zero.");
            }

            BigDecimal lineAmount = BigDecimal.valueOf(itemRequest.getQuantity())
                    .multiply(itemRequest.getUnitPrice());

            BigDecimal gstAmount = calculateGst(
                    lineAmount,
                    itemRequest.getGstPercentage());

            BigDecimal totalPrice = lineAmount.add(gstAmount);

            PurchaseItem purchaseItem = PurchaseItem.builder()
                    .purchase(purchase)
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(itemRequest.getUnitPrice())
                    .gstPercentage(itemRequest.getGstPercentage())
                    .gstAmount(gstAmount)
                    .totalPrice(totalPrice)
                    .build();

            purchase.addPurchaseItem(purchaseItem);

            subtotal = subtotal.add(lineAmount);
            totalGst = totalGst.add(gstAmount);
        }

        BigDecimal discount = request.getDiscountAmount() == null
                ? BigDecimal.ZERO
                : request.getDiscountAmount();

        BigDecimal grandTotal = subtotal
                .add(totalGst)
                .subtract(discount);

        purchase.setSubtotal(subtotal);
        purchase.setGstAmount(totalGst);
        purchase.setDiscountAmount(discount);
        purchase.setTotalAmount(grandTotal);

        Purchase savedPurchase = purchaseRepository.save(purchase);

        return purchaseMapper.toResponse(savedPurchase);
    }

    @Override
    public PurchaseResponse updatePurchase(
            Long id,
            UpdatePurchaseRequest request) {

        Purchase purchase = findPurchase(id);

        if (purchase.getStatus() == PurchaseStatus.RECEIVED) {
            throw new InvalidPurchaseException(
                    "Received purchase cannot be modified.");
        }

        Vendor vendor = findVendor(request.getVendorId());

        purchaseMapper.updateEntity(request, purchase, vendor);

        // Remove existing items
        purchase.getPurchaseItems().clear();

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalGst = BigDecimal.ZERO;

        for (PurchaseItemRequest itemRequest : request.getItems()) {

            Product product = findProduct(itemRequest.getProductId());

            if (itemRequest.getQuantity() == null || itemRequest.getQuantity() <= 0) {
                throw new InvalidPurchaseException(
                        "Quantity must be greater than zero.");
            }

            if (itemRequest.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new InvalidPurchaseException(
                        "Unit price must be greater than zero.");
            }

            BigDecimal lineAmount = BigDecimal.valueOf(itemRequest.getQuantity())
                    .multiply(itemRequest.getUnitPrice());

            BigDecimal gstAmount = calculateGst(
                    lineAmount,
                    itemRequest.getGstPercentage());

            BigDecimal totalPrice = lineAmount.add(gstAmount);

            PurchaseItem purchaseItem = PurchaseItem.builder()
                    .purchase(purchase)
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(itemRequest.getUnitPrice())
                    .gstPercentage(itemRequest.getGstPercentage())
                    .gstAmount(gstAmount)
                    .totalPrice(totalPrice)
                    .build();

            purchase.addPurchaseItem(purchaseItem);

            subtotal = subtotal.add(lineAmount);
            totalGst = totalGst.add(gstAmount);
        }

        BigDecimal discount = request.getDiscountAmount() == null
                ? BigDecimal.ZERO
                : request.getDiscountAmount();

        purchase.setSubtotal(subtotal);
        purchase.setGstAmount(totalGst);
        purchase.setDiscountAmount(discount);
        purchase.setTotalAmount(
                subtotal.add(totalGst).subtract(discount));

        Purchase updatedPurchase = purchaseRepository.save(purchase);

        return purchaseMapper.toResponse(updatedPurchase);
    }

    @Override
    public PurchaseResponse receivePurchase(Long id) {

        Purchase purchase = findPurchase(id);

        if (purchase.getStatus() == PurchaseStatus.RECEIVED) {
            throw new InvalidPurchaseException(
                    "Purchase has already been received.");
        }

        if (purchase.getStatus() != PurchaseStatus.ORDERED) {
            throw new InvalidPurchaseException(
                    "Only ORDERED purchase can be received.");
        }

        updateInventory(purchase);

        purchase.setStatus(PurchaseStatus.RECEIVED);

        Purchase savedPurchase = purchaseRepository.save(purchase);

        return purchaseMapper.toResponse(savedPurchase);
    }

    private void updateInventory(Purchase purchase) {

        for (PurchaseItem item : purchase.getPurchaseItems()) {

            Inventory inventory = inventoryRepository
                    .findByProduct(item.getProduct())
                    .orElseThrow(() ->
                            new InventoryNotFoundException(
                                    "Inventory not found for product : "
                                            + item.getProduct().getName()));

            Integer availableQty = inventory.getAvailableQuantity() == null
                    ? 0
                    : inventory.getAvailableQuantity();

            inventory.setAvailableQuantity(availableQty + item.getQuantity());

            updateInventoryStatus(inventory);

            inventoryRepository.save(inventory);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseResponse getPurchaseById(Long id) {

        Purchase purchase = findPurchase(id);

        return purchaseMapper.toResponse(purchase);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PurchaseResponse> getAllPurchases(
            PageRequestDto pageRequest) {

        Pageable pageable = PageResponseBuilder.buildPageable(pageRequest);

        Page<Purchase> purchasePage =
                purchaseRepository.findAll(pageable);

        Page<PurchaseResponse> responsePage =
                purchasePage.map(purchaseMapper::toResponse);

        return PageResponseBuilder.build(responsePage);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PurchaseResponse> getPurchasesByStatus(
            PurchaseStatus status,
            PageRequestDto pageRequest) {

        Pageable pageable = PageResponseBuilder.buildPageable(pageRequest);

        Page<Purchase> purchasePage =
                purchaseRepository.findByStatus(
                        status,
                        pageable);

        Page<PurchaseResponse> responsePage =
                purchasePage.map(purchaseMapper::toResponse);

        return PageResponseBuilder.build(responsePage);
    }

    
}