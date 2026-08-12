package com.abtd.solarbackend.payment.mapper;

import com.abtd.solarbackend.payment.dto.response.PaymentResponse;
import com.abtd.solarbackend.payment.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentResponse toResponse(Payment payment) {

        PaymentResponse response = new PaymentResponse();

        response.setId(payment.getId());
        response.setPaymentNumber(payment.getPaymentNumber());

        response.setSaleId(payment.getSale().getId());
        response.setSaleNumber(payment.getSale().getSaleNumber());

        response.setCustomerId(payment.getCustomer().getId());
        response.setCustomerName(
                payment.getCustomer().getFirstName() + " "
                        + payment.getCustomer().getLastName());

        response.setAmount(payment.getAmount());
        response.setPaymentDate(payment.getPaymentDate());
        response.setPaymentMode(payment.getPaymentMode());
        response.setPaymentStatus(payment.getPaymentStatus());
        response.setTransactionReference(payment.getTransactionReference());
        response.setRemarks(payment.getRemarks());

        return response;
    }
}