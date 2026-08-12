package com.abtd.solarbackend.payment.dto.request;

import com.abtd.solarbackend.enums.PaymentMode;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CreatePaymentRequest {

    @NotNull(message = "Sale id is required")
    private Long saleId;

    @NotNull(message = "Payment amount is required")
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    @NotNull(message = "Payment date is required")
    private LocalDate paymentDate;

    @NotNull(message = "Payment mode is required")
    private PaymentMode paymentMode;

    private String transactionReference;

    private String remarks;
}