package com.abtd.solarbackend.payment.service;

import com.abtd.solarbackend.common.dto.PageRequestDto;
import com.abtd.solarbackend.common.dto.PageResponse;
import com.abtd.solarbackend.enums.PaymentStatus;
import com.abtd.solarbackend.payment.dto.request.CreatePaymentRequest;
import com.abtd.solarbackend.payment.dto.response.PaymentResponse;

public interface PaymentService {

    PaymentResponse createPayment(CreatePaymentRequest request);

    PaymentResponse getPaymentById(Long id);

    PageResponse<PaymentResponse> getAllPayments(
            PageRequestDto pageRequest);

    PageResponse<PaymentResponse> getPaymentsByStatus(
            PaymentStatus status,
            PageRequestDto pageRequest);

    PageResponse<PaymentResponse> getPaymentsByCustomer(
            Long customerId,
            PageRequestDto pageRequest);

    PageResponse<PaymentResponse> getPaymentsBySale(
            Long saleId,
            PageRequestDto pageRequest);
}