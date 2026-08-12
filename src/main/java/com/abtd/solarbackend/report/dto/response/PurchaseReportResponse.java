package com.abtd.solarbackend.report.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseReportResponse {

    private LocalDate fromDate;

    private LocalDate toDate;

    private long totalPurchases;

    private BigDecimal purchaseAmount;

    private BigDecimal gstAmount;

}