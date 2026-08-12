package com.abtd.solarbackend.quotation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class QuotationItemRequest {

    @NotNull(message = "Product is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity should be greater than zero")
    private Integer quantity;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    private BigDecimal gstPercentage;

    private BigDecimal discountAmount;
}