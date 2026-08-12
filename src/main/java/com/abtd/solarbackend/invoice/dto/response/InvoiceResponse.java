package com.abtd.solarbackend.invoice.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class InvoiceResponse {

    private Long id;

    private String invoiceNumber;

    private Long saleId;

    private String saleNumber;

    private String customerName;

    private LocalDate invoiceDate;

    private LocalDate dueDate;

    private BigDecimal totalAmount;

    private BigDecimal paidAmount;

    private BigDecimal balanceAmount;

    private Boolean paid;
}