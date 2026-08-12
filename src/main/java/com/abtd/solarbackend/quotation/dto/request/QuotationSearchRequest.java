package com.abtd.solarbackend.quotation.dto.request;

import com.abtd.solarbackend.enums.QuotationStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class QuotationSearchRequest {

    private String quotationNumber;

    private Long customerId;

    private QuotationStatus status;

    private LocalDate fromDate;

    private LocalDate toDate;

    private BigDecimal minAmount;

    private BigDecimal maxAmount;

    private Integer page = 0;

    private Integer size = 10;

    private String sortBy = "quotationDate";

    private String direction = "desc";
}