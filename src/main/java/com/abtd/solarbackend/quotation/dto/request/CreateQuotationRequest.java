package com.abtd.solarbackend.quotation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateQuotationRequest {

    @NotNull(message = "Customer is required")
    private Long customerId;

    @NotNull(message = "Quotation date is required")
    private LocalDate quotationDate;

    @NotNull(message = "Valid till date is required")
    private LocalDate validTill;

    private String remarks;

    @Valid
    @NotEmpty(message = "Quotation should contain at least one item")
    private List<QuotationItemRequest> items;
}