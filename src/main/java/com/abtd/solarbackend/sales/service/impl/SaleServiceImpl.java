package com.abtd.solarbackend.sales.service.impl;

import com.abtd.solarbackend.common.dto.PageRequestDto;
import com.abtd.solarbackend.common.dto.PageResponse;
import com.abtd.solarbackend.common.response.PageResponseBuilder;
import com.abtd.solarbackend.customer.entity.Customer;
import com.abtd.solarbackend.customer.exception.CustomerNotFoundException;
import com.abtd.solarbackend.customer.repository.CustomerRepository;
import com.abtd.solarbackend.enums.InventoryStatus;
import com.abtd.solarbackend.enums.SalesStatus;
import com.abtd.solarbackend.inventory.entity.Inventory;
import com.abtd.solarbackend.inventory.exception.InventoryNotFoundException;
import com.abtd.solarbackend.inventory.repository.InventoryRepository;
import com.abtd.solarbackend.product.entity.Product;
import com.abtd.solarbackend.product.exception.ProductNotFoundException;
import com.abtd.solarbackend.product.repository.ProductRepository;
import com.abtd.solarbackend.sales.dto.request.CreateSaleRequest;
import com.abtd.solarbackend.sales.dto.request.SaleItemRequest;
import com.abtd.solarbackend.sales.dto.request.UpdateSaleRequest;
import com.abtd.solarbackend.sales.dto.response.SaleResponse;
import com.abtd.solarbackend.sales.entity.Sale;
import com.abtd.solarbackend.sales.entity.SaleItem;
import com.abtd.solarbackend.sales.exception.InvalidSaleException;
import com.abtd.solarbackend.sales.exception.SaleNotFoundException;
import com.abtd.solarbackend.sales.mapper.SaleMapper;
import com.abtd.solarbackend.sales.repository.SaleRepository;
import com.abtd.solarbackend.sales.service.SaleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
@Transactional
public class SaleServiceImpl implements SaleService {
    private final SaleRepository saleRepository;

    private final CustomerRepository customerRepository;

    private final ProductRepository productRepository;

    private final InventoryRepository inventoryRepository;

    private final SaleMapper saleMapper;

    private Sale findSale(Long id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException(id));
    }

    private Customer findCustomer(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    private Inventory findInventory(Long productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() ->
                        new InventoryNotFoundException(
                                "Inventory not found for product : " + productId));
    }

    private String generateSaleNumber() {

        return saleRepository.findTopByOrderByIdDesc()
                .map(sale -> {

                    String lastNumber = sale.getSaleNumber();

                    int number = Integer.parseInt(
                            lastNumber.substring(3));

                    return String.format(
                            "SAL%06d",
                            number + 1);

                })
                .orElse("SAL000001");
    }

    private BigDecimal calculateGst(
            BigDecimal amount,
            BigDecimal gstPercentage) {

        return amount.multiply(gstPercentage)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    private void validateStock(
            Inventory inventory,
            BigDecimal quantity) {

        if (inventory.getAvailableQuantity()
                .compareTo(quantity.intValue()) < 0) {

            throw new InvalidSaleException(
                    "Insufficient stock for product : "
                            + inventory.getProduct().getName());
        }
    }

    private SaleItem createSaleItem(
            Product product,
            SaleItemRequest request) {

        BigDecimal itemAmount =
                request.getQuantity()
                        .multiply(request.getUnitPrice());

        BigDecimal gstAmount =
                calculateGst(
                        itemAmount,
                        request.getGstPercentage());

        return SaleItem.builder()
                .product(product)
                .quantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                .gstPercentage(request.getGstPercentage())
                .gstAmount(gstAmount)
                .totalPrice(itemAmount.add(gstAmount))
                .build();
    }

    private void calculateSaleTotals(Sale sale) {

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal gstAmount = BigDecimal.ZERO;

        for (SaleItem item : sale.getSaleItems()) {

            BigDecimal itemSubtotal =
                    item.getQuantity()
                            .multiply(item.getUnitPrice());

            subtotal = subtotal.add(itemSubtotal);

            gstAmount = gstAmount.add(item.getGstAmount());
        }

        BigDecimal discount =
                sale.getDiscountAmount() == null
                        ? BigDecimal.ZERO
                        : sale.getDiscountAmount();

        sale.setSubtotal(subtotal);
        sale.setGstAmount(gstAmount);

        sale.setTotalAmount(
                subtotal
                        .add(gstAmount)
                        .subtract(discount));
    }

    private void reduceInventory(Sale sale) {

        for (SaleItem item : sale.getSaleItems()) {

            Inventory inventory =
                    findInventory(item.getProduct().getId());

            validateStock(
                    inventory,
                    item.getQuantity());

            inventory.setAvailableQuantity(
                    inventory.getAvailableQuantity() - item.getQuantity().intValue());

            updateInventoryStatus(inventory);

            inventoryRepository.save(inventory);
        }
    }

    private void updateInventoryStatus(
            Inventory inventory) {

        if (inventory.getAvailableQuantity() == 0) {

            inventory.setStatus(InventoryStatus.OUT_OF_STOCK);

        } else if (inventory.getAvailableQuantity()
                <= inventory.getMinimumStock()) {

            inventory.setStatus(InventoryStatus.LOW_STOCK);

        } else {

            inventory.setStatus(InventoryStatus.AVAILABLE);
        }
    }

    @Override
    public SaleResponse createSale(CreateSaleRequest request) {

        Customer customer = findCustomer(request.getCustomerId());

        Sale sale = Sale.builder()
                .saleNumber(generateSaleNumber())
                .customer(customer)
                .saleDate(request.getSaleDate())
                .invoiceNumber(request.getInvoiceNumber())
                .invoiceDate(request.getInvoiceDate())
                .discountAmount(
                        request.getDiscountAmount() == null
                                ? BigDecimal.ZERO
                                : request.getDiscountAmount())
                .remarks(request.getRemarks())
                .status(SalesStatus.DRAFT)
                .build();

        for (SaleItemRequest itemRequest : request.getItems()) {

            Product product = findProduct(itemRequest.getProductId());

            Inventory inventory = findInventory(product.getId());

            validateStock(
                    inventory,
                    itemRequest.getQuantity());

            SaleItem saleItem = createSaleItem(
                    product,
                    itemRequest);

            sale.addSaleItem(saleItem);
        }

        calculateSaleTotals(sale);

        Sale savedSale = saleRepository.save(sale);

        return saleMapper.toResponse(savedSale);
    }

    @Override
    public SaleResponse updateSale(
            Long id,
            UpdateSaleRequest request) {

        Sale sale = findSale(id);

        if (sale.getStatus() != SalesStatus.DRAFT) {
            throw new InvalidSaleException(
                    "Only draft sales can be updated.");
        }

        Customer customer = findCustomer(request.getCustomerId());

        sale.setCustomer(customer);
        sale.setSaleDate(request.getSaleDate());
        sale.setInvoiceNumber(request.getInvoiceNumber());
        sale.setInvoiceDate(request.getInvoiceDate());
        sale.setDiscountAmount(
                request.getDiscountAmount() == null
                        ? BigDecimal.ZERO
                        : request.getDiscountAmount());
        sale.setRemarks(request.getRemarks());

        // Remove existing items
        sale.getSaleItems().clear();

        // Add updated items
        for (SaleItemRequest itemRequest : request.getItems()) {

            Product product = findProduct(itemRequest.getProductId());

            Inventory inventory = findInventory(product.getId());

            validateStock(
                    inventory,
                    itemRequest.getQuantity());

            SaleItem saleItem = createSaleItem(
                    product,
                    itemRequest);

            sale.addSaleItem(saleItem);
        }

        calculateSaleTotals(sale);

        Sale updatedSale = saleRepository.save(sale);

        return saleMapper.toResponse(updatedSale);
    }

    @Override
    public SaleResponse completeSale(Long id) {

        Sale sale = findSale(id);

        if (sale.getStatus() == SalesStatus.COMPLETED) {
            throw new InvalidSaleException(
                    "Sale is already completed.");
        }

        if (sale.getStatus() == SalesStatus.CANCELLED) {
            throw new InvalidSaleException(
                    "Cancelled sale cannot be completed.");
        }

        // Validate stock for all items before updating inventory
        for (SaleItem item : sale.getSaleItems()) {

            Inventory inventory = findInventory(
                    item.getProduct().getId());

            validateStock(
                    inventory,
                    item.getQuantity());
        }

        // Deduct inventory
        reduceInventory(sale);

        sale.setStatus(SalesStatus.COMPLETED);

        Sale completedSale = saleRepository.save(sale);

        return saleMapper.toResponse(completedSale);
    }

    @Override
    @Transactional
    public SaleResponse getSaleById(Long id) {

        Sale sale = findSale(id);

        return saleMapper.toResponse(sale);
    }

    @Override
    @Transactional
    public PageResponse<SaleResponse> getAllSales(
            PageRequestDto pageRequest) {

        Pageable pageable =
                PageResponseBuilder.buildPageable(pageRequest);

        Page<Sale> sales =
                saleRepository.findAll(pageable);

        return PageResponseBuilder.build(
                sales,
                saleMapper::toResponse);
    }

    @Override
    @Transactional
    public PageResponse<SaleResponse> getSalesByStatus(
            SalesStatus status,
            PageRequestDto pageRequest) {

        Pageable pageable =
                PageResponseBuilder.buildPageable(pageRequest);

        Page<Sale> sales =
                saleRepository.findByStatus(
                        status,
                        pageable);

        return PageResponseBuilder.build(
                sales,
                saleMapper::toResponse);
    }

}