package com.abtd.solarbackend.quotation.service.impl;

import com.abtd.solarbackend.common.dto.PageRequestDto;
import com.abtd.solarbackend.customer.entity.Customer;
import com.abtd.solarbackend.customer.exception.CustomerNotFoundException;
import com.abtd.solarbackend.customer.repository.CustomerRepository;
import com.abtd.solarbackend.enums.PaymentStatus;
import com.abtd.solarbackend.enums.QuotationStatus;
import com.abtd.solarbackend.enums.SalesStatus;
import com.abtd.solarbackend.exception.BadRequestException;
import com.abtd.solarbackend.exception.ResourceNotFoundException;
import com.abtd.solarbackend.inventory.entity.Inventory;
import com.abtd.solarbackend.inventory.repository.InventoryRepository;
import com.abtd.solarbackend.product.entity.Product;
import com.abtd.solarbackend.product.exception.ProductNotFoundException;
import com.abtd.solarbackend.product.repository.ProductRepository;
import com.abtd.solarbackend.quotation.dto.request.CreateQuotationRequest;
import com.abtd.solarbackend.quotation.dto.request.QuotationItemRequest;
import com.abtd.solarbackend.quotation.dto.request.QuotationSearchRequest;
import com.abtd.solarbackend.quotation.dto.request.UpdateQuotationRequest;
import com.abtd.solarbackend.quotation.dto.response.QuotationDashboardResponse;
import com.abtd.solarbackend.quotation.dto.response.QuotationResponse;
import com.abtd.solarbackend.quotation.entity.Quotation;
import com.abtd.solarbackend.quotation.entity.QuotationItem;
import com.abtd.solarbackend.quotation.mapper.QuotationMapper;
import com.abtd.solarbackend.quotation.repository.QuotationRepository;
import com.abtd.solarbackend.quotation.service.QuotationService;
import com.abtd.solarbackend.sales.entity.Sale;
import com.abtd.solarbackend.sales.entity.SaleItem;
import com.abtd.solarbackend.sales.repository.SaleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import com.abtd.solarbackend.quotation.specification.QuotationSpecification;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QuotationServiceImpl implements QuotationService {

    private final QuotationRepository quotationRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final QuotationMapper quotationMapper;
    private final SaleRepository saleRepository;

    private final InventoryRepository inventoryRepository;

    private String generateQuotationNumber() {

        return quotationRepository.findTopByOrderByIdDesc()
                .map(q -> {
                    int next = Integer.parseInt(
                            q.getQuotationNumber().substring(3)) + 1;

                    return String.format("QUO%06d", next);
                })
                .orElse("QUO000001");
    }

    private Product getProduct(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id : " + id));
    }

    private Customer getCustomer(Long id) {

        return customerRepository.findById(id)
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found with id : " + id));
    }

    private Quotation getQuotation(Long id) {

        return quotationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Quotation not found with id : " + id));
    }

    private BigDecimal calculateGST(
            BigDecimal amount,
            BigDecimal gstPercentage) {

        if (gstPercentage == null) {
            return BigDecimal.ZERO;
        }

        return amount.multiply(gstPercentage)
                .divide(BigDecimal.valueOf(100),
                        2,
                        RoundingMode.HALF_UP);
    }

    @Override
    public QuotationResponse createQuotation(
            CreateQuotationRequest request) {

        Customer customer = getCustomer(request.getCustomerId());

        Quotation quotation = new Quotation();

        quotation.setQuotationNumber(generateQuotationNumber());
        quotation.setCustomer(customer);
        quotation.setQuotationDate(request.getQuotationDate());
        quotation.setValidTill(request.getValidTill());
        quotation.setRemarks(request.getRemarks());
        quotation.setStatus(QuotationStatus.DRAFT);

        List<QuotationItem> quotationItems = new ArrayList<>();

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalGST = BigDecimal.ZERO;
        BigDecimal totalDiscount = BigDecimal.ZERO;
        BigDecimal grandTotal = BigDecimal.ZERO;

        for (QuotationItemRequest itemRequest : request.getItems()) {

            Product product = getProduct(itemRequest.getProductId());

            BigDecimal quantity =
                    BigDecimal.valueOf(itemRequest.getQuantity());

            BigDecimal unitPrice = itemRequest.getPrice();

            BigDecimal lineAmount =
                    unitPrice.multiply(quantity);

            BigDecimal gstAmount =
                    calculateGST(
                            lineAmount,
                            itemRequest.getGstPercentage());

            BigDecimal discountAmount =
                    itemRequest.getDiscountAmount() == null
                            ? BigDecimal.ZERO
                            : itemRequest.getDiscountAmount();

            BigDecimal lineTotal =
                    lineAmount
                            .add(gstAmount)
                            .subtract(discountAmount);

            QuotationItem quotationItem =
                    QuotationItem.builder()
                            .quotation(quotation)
                            .product(product)
                            .quantity(itemRequest.getQuantity())
                            .price(unitPrice)
                            .gstPercentage(itemRequest.getGstPercentage())
                            .discountAmount(discountAmount)
                            .lineTotal(lineTotal)
                            .build();

            quotationItems.add(quotationItem);

            subtotal = subtotal.add(lineAmount);

            totalGST = totalGST.add(gstAmount);

            totalDiscount = totalDiscount.add(discountAmount);

            grandTotal = grandTotal.add(lineTotal);
        }

        quotation.setQuotationItems(quotationItems);

        quotation.setSubtotal(subtotal);

        quotation.setGstAmount(totalGST);

        quotation.setDiscountAmount(totalDiscount);

        quotation.setTotalAmount(grandTotal);

        Quotation savedQuotation =
                quotationRepository.save(quotation);

        return quotationMapper.toResponse(savedQuotation);
    }

    @Override
    public QuotationResponse getQuotationById(Long id) {

        Quotation quotation = getQuotation(id);

        return quotationMapper.toResponse(quotation);
    }

    @Override
    public Page<QuotationResponse> getAllQuotations(
            PageRequestDto request) {

        Sort sort = request.getDirection().equalsIgnoreCase("asc")
                ? Sort.by(request.getSortBy()).ascending()
                : Sort.by(request.getSortBy()).descending();

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                sort);

        return quotationRepository.findAll(pageable)
                .map(quotationMapper::toResponse);
    }

    @Override
    public void deleteQuotation(Long id) {

        Quotation quotation = getQuotation(id);

        if (quotation.getStatus() == QuotationStatus.CONVERTED) {
            throw new RuntimeException(
                    "Converted quotation cannot be deleted.");
        }

        quotationRepository.delete(quotation);
    }

    @Override
    public QuotationResponse updateQuotation(
            Long id,
            UpdateQuotationRequest request) {

        Quotation quotation = getQuotation(id);

        if (quotation.getStatus() == QuotationStatus.CONVERTED) {
            throw new RuntimeException(
                    "Converted quotation cannot be updated.");
        }

        quotation.setQuotationDate(request.getQuotationDate());
        quotation.setValidTill(request.getValidTill());
        quotation.setRemarks(request.getRemarks());

        // Remove existing items
        quotation.getQuotationItems().clear();

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalGST = BigDecimal.ZERO;
        BigDecimal totalDiscount = BigDecimal.ZERO;
        BigDecimal grandTotal = BigDecimal.ZERO;

        for (QuotationItemRequest itemRequest : request.getItems()) {

            Product product = getProduct(itemRequest.getProductId());

            BigDecimal quantity =
                    BigDecimal.valueOf(itemRequest.getQuantity());

            BigDecimal lineAmount =
                    itemRequest.getPrice().multiply(quantity);

            BigDecimal gstAmount =
                    calculateGST(
                            lineAmount,
                            itemRequest.getGstPercentage());

            BigDecimal discountAmount =
                    itemRequest.getDiscountAmount() == null
                            ? BigDecimal.ZERO
                            : itemRequest.getDiscountAmount();

            BigDecimal lineTotal =
                    lineAmount
                            .add(gstAmount)
                            .subtract(discountAmount);

            QuotationItem quotationItem =
                    QuotationItem.builder()
                            .quotation(quotation)
                            .product(product)
                            .quantity(itemRequest.getQuantity())
                            .price(itemRequest.getPrice())
                            .gstPercentage(itemRequest.getGstPercentage())
                            .discountAmount(discountAmount)
                            .lineTotal(lineTotal)
                            .build();

            quotation.getQuotationItems().add(quotationItem);

            subtotal = subtotal.add(lineAmount);
            totalGST = totalGST.add(gstAmount);
            totalDiscount = totalDiscount.add(discountAmount);
            grandTotal = grandTotal.add(lineTotal);
        }

        quotation.setSubtotal(subtotal);
        quotation.setGstAmount(totalGST);
        quotation.setDiscountAmount(totalDiscount);
        quotation.setTotalAmount(grandTotal);

        Quotation updatedQuotation =
                quotationRepository.save(quotation);

        return quotationMapper.toResponse(updatedQuotation);
    }

    @Override
    public QuotationResponse approveQuotation(Long id) {

        Quotation quotation = getQuotation(id);

        if (quotation.getStatus() == QuotationStatus.CONVERTED) {
            throw new RuntimeException(
                    "Quotation is already converted.");
        }

        if (quotation.getStatus() == QuotationStatus.APPROVED) {
            throw new RuntimeException(
                    "Quotation is already approved.");
        }

        if (quotation.getValidTill().isBefore(LocalDate.now())) {
            throw new RuntimeException(
                    "Quotation has expired.");
        }

        quotation.setStatus(QuotationStatus.APPROVED);

        Quotation savedQuotation =
                quotationRepository.save(quotation);

        return quotationMapper.toResponse(savedQuotation);
    }

    @Override
    @Transactional
    public Long convertToSale(Long quotationId) {

        Quotation quotation = getQuotation(quotationId);

        if (quotation.getStatus() != QuotationStatus.APPROVED) {
            throw new RuntimeException(
                    "Only approved quotations can be converted.");
        }

        Sale sale = Sale.builder()
                .saleNumber(generateSaleNumber())
                .customer(quotation.getCustomer())
                .saleDate(LocalDate.now())
                .invoiceDate(LocalDate.now())
                .subtotal(quotation.getSubtotal())
                .gstAmount(quotation.getGstAmount())
                .discountAmount(quotation.getDiscountAmount())
                .totalAmount(quotation.getTotalAmount())
                .balanceAmount(quotation.getTotalAmount())
                .paidAmount(BigDecimal.ZERO)
                .paymentStatus(PaymentStatus.PENDING)
                .status(SalesStatus.DRAFT)
                .remarks("Converted from Quotation : "
                        + quotation.getQuotationNumber())
                .build();

        for (QuotationItem quotationItem : quotation.getQuotationItems()) {

            Product product = quotationItem.getProduct();

            Inventory inventory = inventoryRepository
                    .findByProduct(product)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Inventory not found for "
                                            + product.getName()));

            if (inventory.getAvailableQuantity()
                    < quotationItem.getQuantity()) {

                throw new RuntimeException(
                        "Insufficient stock for "
                                + product.getName());
            }

            inventory.setAvailableQuantity(
                    inventory.getAvailableQuantity()
                            - quotationItem.getQuantity());

            inventoryRepository.save(inventory);

            BigDecimal quantity = BigDecimal.valueOf(quotationItem.getQuantity());

            BigDecimal lineAmount =
                    quotationItem.getPrice().multiply(quantity);

            BigDecimal gstAmount =
                    calculateGST(
                            lineAmount,
                            quotationItem.getGstPercentage());

            BigDecimal totalPrice =
                    lineAmount.add(gstAmount);

            SaleItem saleItem = SaleItem.builder()
                    .sale(sale)
                    .product(product)
                    .quantity(quantity)
                    .unitPrice(quotationItem.getPrice())
                    .gstPercentage(quotationItem.getGstPercentage())
                    .gstAmount(gstAmount)
                    .totalPrice(totalPrice)
                    .build();

            sale.addSaleItem(saleItem);
        }

        Sale savedSale = saleRepository.save(sale);

        quotation.setStatus(QuotationStatus.CONVERTED);

        quotationRepository.save(quotation);

        return savedSale.getId();
    }

    private String generateSaleNumber() {

        return saleRepository.findTopByOrderByIdDesc()
                .map(sale -> {
                    String lastSaleNumber = sale.getSaleNumber();

                    int nextNumber = Integer.parseInt(
                            lastSaleNumber.substring(3)) + 1;

                    return String.format("SAL%06d", nextNumber);
                })
                .orElse("SAL000001");
    }

    @Override
    public QuotationDashboardResponse getDashboard() {

        return QuotationDashboardResponse.builder()

                .totalQuotations(
                        quotationRepository.count())

                .draftQuotations(
                        quotationRepository.countByStatus(
                                QuotationStatus.DRAFT))

                .approvedQuotations(
                        quotationRepository.countByStatus(
                                QuotationStatus.APPROVED))

                .convertedQuotations(
                        quotationRepository.countByStatus(
                                QuotationStatus.CONVERTED))

                .rejectedQuotations(
                        quotationRepository.countByStatus(
                                QuotationStatus.REJECTED))

                .expiredQuotations(
                        quotationRepository.countByStatus(
                                QuotationStatus.EXPIRED))

                .totalQuotationAmount(
                        quotationRepository.getTotalQuotationAmount())

                .build();
    }

    @Override
    public Page<QuotationResponse> searchQuotations(
            QuotationSearchRequest request) {

        Sort sort = request.getDirection().equalsIgnoreCase("asc")
                ? Sort.by(request.getSortBy()).ascending()
                : Sort.by(request.getSortBy()).descending();

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                sort);

        return quotationRepository.findAll(

                        QuotationSpecification.search(request),

                        pageable)

                .map(quotationMapper::toResponse);
    }
}