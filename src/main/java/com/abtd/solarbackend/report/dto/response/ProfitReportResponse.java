package com.abtd.solarbackend.report.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfitReportResponse {

    private BigDecimal salesAmount;

    private BigDecimal purchaseAmount;

    private BigDecimal profit;

}