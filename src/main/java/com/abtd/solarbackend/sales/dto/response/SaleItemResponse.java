package com.abtd.solarbackend.sales.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SaleItemResponse {

    private Long id;

    private Long productId;

    private String productName;

    private BigDecimal quantity;

    private BigDecimal unitPrice;

    private BigDecimal gstPercentage;

    private BigDecimal gstAmount;

    private BigDecimal totalPrice;
}