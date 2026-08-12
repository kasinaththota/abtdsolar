package com.abtd.solarbackend.quotation.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class QuotationItemResponse {

    private Long id;

    private Long productId;

    private String productCode;

    private String productName;

    private Integer quantity;

    private BigDecimal price;

    private BigDecimal gstPercentage;

    private BigDecimal discountAmount;

    private BigDecimal lineTotal;
}