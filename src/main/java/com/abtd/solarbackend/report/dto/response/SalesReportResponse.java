package com.abtd.solarbackend.report.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesReportResponse {

    private LocalDate fromDate;

    private LocalDate toDate;

    private long totalSales;

    private BigDecimal salesAmount;

    private BigDecimal gstAmount;

    private BigDecimal discountAmount;

    private BigDecimal netAmount;

}