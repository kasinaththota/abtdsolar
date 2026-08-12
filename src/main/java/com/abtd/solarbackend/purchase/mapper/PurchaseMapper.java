package com.abtd.solarbackend.purchase.mapper;

import com.abtd.solarbackend.purchase.dto.request.CreatePurchaseRequest;
import com.abtd.solarbackend.purchase.dto.request.UpdatePurchaseRequest;
import com.abtd.solarbackend.purchase.dto.response.PurchaseItemResponse;
import com.abtd.solarbackend.purchase.dto.response.PurchaseResponse;
import com.abtd.solarbackend.purchase.entity.Purchase;
import com.abtd.solarbackend.purchase.entity.PurchaseItem;
import com.abtd.solarbackend.vendor.entity.Vendor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PurchaseMapper {

    /**
     * Create Purchase Entity
     */
    public Purchase toEntity(CreatePurchaseRequest request,
                             Vendor vendor) {

        return Purchase.builder()
                .vendor(vendor)
                .purchaseDate(request.getPurchaseDate())
                .invoiceNumber(request.getInvoiceNumber())
                .invoiceDate(request.getInvoiceDate())
                .discountAmount(request.getDiscountAmount())
                .remarks(request.getRemarks())
                .build();
    }

    /**
     * Entity -> Response
     */
    public PurchaseResponse toResponse(Purchase purchase) {

        List<PurchaseItemResponse> itemResponses =
                purchase.getPurchaseItems()
                        .stream()
                        .map(this::toItemResponse)
                        .collect(Collectors.toList());

        return PurchaseResponse.builder()
                .id(purchase.getId())
                .purchaseNumber(purchase.getPurchaseNumber())
                .vendorId(purchase.getVendor().getId())
                .vendorCode(purchase.getVendor().getVendorCode())
                .vendorName(purchase.getVendor().getCompanyName())
                .purchaseDate(purchase.getPurchaseDate())
                .invoiceNumber(purchase.getInvoiceNumber())
                .invoiceDate(purchase.getInvoiceDate())
                .subtotal(purchase.getSubtotal())
                .gstAmount(purchase.getGstAmount())
                .discountAmount(purchase.getDiscountAmount())
                .totalAmount(purchase.getTotalAmount())
                .remarks(purchase.getRemarks())
                .status(purchase.getStatus())
                .items(itemResponses)
                .createdAt(purchase.getCreatedAt())
                .updatedAt(purchase.getUpdatedAt())
                .build();
    }

    /**
     * PurchaseItem -> PurchaseItemResponse
     */
    public PurchaseItemResponse toItemResponse(PurchaseItem item) {

        return PurchaseItemResponse.builder()
                .productId(item.getProduct().getId())
                .productCode(item.getProduct().getProductCode())
                .productName(item.getProduct().getName())
                .quantity(BigDecimal.valueOf(item.getQuantity()))
                .unitPrice(item.getUnitPrice())
                .gstPercentage(item.getGstPercentage())
                .gstAmount(item.getGstAmount())
                .totalPrice(item.getTotalPrice())
                .build();
    }

    /**
     * Update Purchase
     */
    public void updateEntity(UpdatePurchaseRequest request,
                             Purchase purchase,
                             Vendor vendor) {

        purchase.setVendor(vendor);
        purchase.setPurchaseDate(request.getPurchaseDate());
        purchase.setInvoiceNumber(request.getInvoiceNumber());
        purchase.setInvoiceDate(request.getInvoiceDate());
        purchase.setDiscountAmount(request.getDiscountAmount());
        purchase.setRemarks(request.getRemarks());
    }
}