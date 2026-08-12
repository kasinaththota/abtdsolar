package com.abtd.solarbackend.purchase.dto.response;

import com.abtd.solarbackend.enums.PurchaseStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseResponse {

    private Long id;

    private String purchaseNumber;

    private Long vendorId;

    private String vendorCode;

    private String vendorName;

    private LocalDate purchaseDate;

    private String invoiceNumber;

    private LocalDate invoiceDate;

    private BigDecimal subtotal;

    private BigDecimal gstAmount;

    private BigDecimal discountAmount;

    private BigDecimal totalAmount;

    private String remarks;

    private PurchaseStatus status;

    private List<PurchaseItemResponse> items;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}