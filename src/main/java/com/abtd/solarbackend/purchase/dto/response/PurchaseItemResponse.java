package com.abtd.solarbackend.purchase.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseItemResponse {

    private Long productId;

    private String productCode;

    private String productName;

    private BigDecimal quantity;

    private BigDecimal unitPrice;

    private BigDecimal gstPercentage;

    private BigDecimal gstAmount;

    private BigDecimal totalPrice;
}