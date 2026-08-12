package com.abtd.solarbackend.payment.dto.response;

import com.abtd.solarbackend.enums.PaymentMode;
import com.abtd.solarbackend.enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PaymentResponse {

    private Long id;

    private String paymentNumber;

    private Long saleId;

    private String saleNumber;

    private Long customerId;

    private String customerName;

    private BigDecimal amount;

    private LocalDate paymentDate;

    private PaymentMode paymentMode;

    private PaymentStatus paymentStatus;

    private String transactionReference;

    private String remarks;
}