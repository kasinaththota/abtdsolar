package com.abtd.solarbackend.purchase.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePurchaseRequest {

    @NotNull(message = "Vendor Id is required")
    private Long vendorId;

    @NotNull(message = "Purchase date is required")
    private LocalDate purchaseDate;

    private String invoiceNumber;

    private LocalDate invoiceDate;

    @Builder.Default
    private java.math.BigDecimal discountAmount =
            java.math.BigDecimal.ZERO;

    private String remarks;

    @Valid
    @NotEmpty(message = "At least one purchase item is required")
    private List<PurchaseItemRequest> items;
}