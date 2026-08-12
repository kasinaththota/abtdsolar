package com.abtd.solarbackend.quotation.dto.response;

import com.abtd.solarbackend.enums.QuotationStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class QuotationResponse {

    private Long id;

    private String quotationNumber;

    private Long customerId;

    private String customerName;

    private LocalDate quotationDate;

    private LocalDate validTill;

    private BigDecimal subtotal;

    private BigDecimal gstAmount;

    private BigDecimal discountAmount;

    private BigDecimal totalAmount;

    private String remarks;

    private QuotationStatus status;

    private List<QuotationItemResponse> items;
}