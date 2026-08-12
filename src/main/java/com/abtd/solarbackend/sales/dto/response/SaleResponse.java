package com.abtd.solarbackend.sales.dto.response;

import com.abtd.solarbackend.enums.SalesStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class SaleResponse {

    private Long id;

    private String saleNumber;

    private Long customerId;

    private String customerName;

    private LocalDate saleDate;

    private String invoiceNumber;

    private LocalDate invoiceDate;

    private BigDecimal subtotal;

    private BigDecimal gstAmount;

    private BigDecimal discountAmount;

    private BigDecimal totalAmount;

    private String remarks;

    private SalesStatus status;

    private List<SaleItemResponse> saleItems;
}