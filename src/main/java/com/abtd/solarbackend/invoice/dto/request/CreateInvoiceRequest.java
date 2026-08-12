package com.abtd.solarbackend.invoice.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateInvoiceRequest {

    @NotNull
    private Long saleId;

    @NotNull
    @FutureOrPresent
    private LocalDate dueDate;
}