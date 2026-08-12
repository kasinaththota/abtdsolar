package com.abtd.solarbackend.sales.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class UpdateSaleRequest {

    @NotNull(message = "Customer id is required")
    private Long customerId;

    @NotNull(message = "Sale date is required")
    private LocalDate saleDate;

    private String invoiceNumber;

    private LocalDate invoiceDate;

    private BigDecimal discountAmount = BigDecimal.ZERO;

    private String remarks;

    @Valid
    @NotEmpty(message = "At least one sale item is required")
    private List<SaleItemRequest> items;
}