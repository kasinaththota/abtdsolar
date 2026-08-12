package com.abtd.solarbackend.payment.controller;

import com.abtd.solarbackend.common.constants.PaymentMessages;
import com.abtd.solarbackend.common.dto.PageRequestDto;
import com.abtd.solarbackend.common.dto.PageResponse;
import com.abtd.solarbackend.common.response.ApiResponse;
import com.abtd.solarbackend.common.response.ResponseBuilder;
import com.abtd.solarbackend.enums.PaymentStatus;
import com.abtd.solarbackend.payment.dto.request.CreatePaymentRequest;
import com.abtd.solarbackend.payment.dto.response.PaymentResponse;
import com.abtd.solarbackend.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','ACCOUNTANT')")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> createPayment(
            @Valid @RequestBody CreatePaymentRequest request) {

        return ResponseBuilder.created(
                PaymentMessages.PAYMENT_CREATED,
                paymentService.createPayment(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentById(
            @PathVariable Long id) {

        return ResponseBuilder.ok(
                PaymentMessages.PAYMENT_FETCHED,
                paymentService.getPaymentById(id));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PaymentResponse>>> getAllPayments(
            @ModelAttribute PageRequestDto pageRequest) {

        return ResponseBuilder.ok(
                PaymentMessages.PAYMENTS_FETCHED,
                paymentService.getAllPayments(pageRequest));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<PageResponse<PaymentResponse>>> getPaymentsByStatus(
            @PathVariable PaymentStatus status,
            @ModelAttribute PageRequestDto pageRequest) {

        return ResponseBuilder.ok(
                PaymentMessages.PAYMENTS_FETCHED,
                paymentService.getPaymentsByStatus(status, pageRequest));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<PageResponse<PaymentResponse>>> getPaymentsByCustomer(
            @PathVariable Long customerId,
            @ModelAttribute PageRequestDto pageRequest) {

        return ResponseBuilder.ok(
                PaymentMessages.PAYMENTS_FETCHED,
                paymentService.getPaymentsByCustomer(customerId, pageRequest));
    }

    @GetMapping("/sale/{saleId}")
    public ResponseEntity<ApiResponse<PageResponse<PaymentResponse>>> getPaymentsBySale(
            @PathVariable Long saleId,
            @ModelAttribute PageRequestDto pageRequest) {

        return ResponseBuilder.ok(
                PaymentMessages.PAYMENTS_FETCHED,
                paymentService.getPaymentsBySale(saleId, pageRequest));
    }
}