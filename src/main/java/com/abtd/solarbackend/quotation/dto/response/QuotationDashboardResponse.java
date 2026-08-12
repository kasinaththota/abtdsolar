package com.abtd.solarbackend.quotation.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class QuotationDashboardResponse {

    private long totalQuotations;

    private long draftQuotations;

    private long approvedQuotations;

    private long convertedQuotations;

    private long rejectedQuotations;

    private long expiredQuotations;

    private BigDecimal totalQuotationAmount;
}