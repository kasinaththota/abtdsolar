package com.abtd.solarbackend.quotation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateQuotationRequest {

    @NotNull(message = "Quotation date is required")
    private LocalDate quotationDate;

    @NotNull(message = "Valid till date is required")
    private LocalDate validTill;

    private String remarks;

    @Valid
    private List<QuotationItemRequest> items;
}