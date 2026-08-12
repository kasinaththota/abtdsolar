package com.abtd.solarbackend.payment.service.impl;

import com.abtd.solarbackend.common.dto.PageRequestDto;
import com.abtd.solarbackend.common.dto.PageResponse;
import com.abtd.solarbackend.common.response.PageResponseBuilder;
import com.abtd.solarbackend.customer.entity.Customer;
import com.abtd.solarbackend.enums.PaymentStatus;
import com.abtd.solarbackend.payment.dto.request.CreatePaymentRequest;
import com.abtd.solarbackend.payment.dto.response.PaymentResponse;
import com.abtd.solarbackend.payment.entity.Payment;
import com.abtd.solarbackend.payment.exception.InvalidPaymentException;
import com.abtd.solarbackend.payment.exception.PaymentNotFoundException;
import com.abtd.solarbackend.payment.mapper.PaymentMapper;
import com.abtd.solarbackend.payment.repository.PaymentRepository;
import com.abtd.solarbackend.payment.service.PaymentService;
import com.abtd.solarbackend.sales.entity.Sale;
import com.abtd.solarbackend.sales.exception.SaleNotFoundException;
import com.abtd.solarbackend.sales.repository.SaleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final SaleRepository saleRepository;

    private final PaymentMapper paymentMapper;

    private Payment findPayment(Long id) {

        return paymentRepository.findById(id)
                .orElseThrow(() ->
                        new PaymentNotFoundException(id));
    }

    private Sale findSale(Long id) {

        return saleRepository.findById(id)
                .orElseThrow(() ->
                        new SaleNotFoundException(id));
    }

    private String generatePaymentNumber() {

        return paymentRepository.findTopByOrderByIdDesc()
                .map(payment -> {

                    int number = Integer.parseInt(
                            payment.getPaymentNumber().substring(3));

                    return String.format(
                            "PAY%06d",
                            number + 1);

                })
                .orElse("PAY000001");
    }

    private void validatePayment(
            Sale sale,
            BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new InvalidPaymentException(
                    "Payment amount should be greater than zero.");
        }

        if (amount.compareTo(sale.getBalanceAmount()) > 0) {

            throw new InvalidPaymentException(
                    "Payment exceeds pending balance.");
        }
    }

    private void updateSalePayment(
            Sale sale,
            BigDecimal amount) {

        sale.setPaidAmount(
                sale.getPaidAmount().add(amount));

        sale.setBalanceAmount(
                sale.getTotalAmount()
                        .subtract(sale.getPaidAmount()));

        if (sale.getBalanceAmount()
                .compareTo(BigDecimal.ZERO) == 0) {

            sale.setPaymentStatus(
                    PaymentStatus.PAID);

        } else {

            sale.setPaymentStatus(
                    PaymentStatus.PARTIAL);
        }

        saleRepository.save(sale);
    }

    @Override
    public PaymentResponse createPayment(
            CreatePaymentRequest request) {

        Sale sale = findSale(request.getSaleId());

        validatePayment(
                sale,
                request.getAmount());

        Payment payment = Payment.builder()
                .paymentNumber(generatePaymentNumber())
                .sale(sale)
                .customer(sale.getCustomer())
                .amount(request.getAmount())
                .paymentDate(request.getPaymentDate())
                .paymentMode(request.getPaymentMode())
                .paymentStatus(PaymentStatus.PAID)
                .transactionReference(request.getTransactionReference())
                .remarks(request.getRemarks())
                .build();

        Payment savedPayment =
                paymentRepository.save(payment);

        updateSalePayment(
                sale,
                request.getAmount());

        return paymentMapper.toResponse(savedPayment);
    }

    @Override
    @Transactional
    public PaymentResponse getPaymentById(Long id) {

        Payment payment = findPayment(id);

        return paymentMapper.toResponse(payment);
    }

    @Override
    @Transactional
    public PageResponse<PaymentResponse> getAllPayments(
            PageRequestDto pageRequest) {

        Pageable pageable =
                PageResponseBuilder.buildPageable(pageRequest);

        Page<Payment> payments =
                paymentRepository.findAll(pageable);

        return PageResponseBuilder.build(
                payments,
                paymentMapper::toResponse);
    }

    @Override
    @Transactional
    public PageResponse<PaymentResponse> getPaymentsByStatus(
            PaymentStatus status,
            PageRequestDto pageRequest) {

        Pageable pageable =
                PageResponseBuilder.buildPageable(pageRequest);

        Page<Payment> payments =
                paymentRepository.findByPaymentStatus(
                        status,
                        pageable);

        return PageResponseBuilder.build(
                payments,
                paymentMapper::toResponse);
    }

    @Override
    @Transactional
    public PageResponse<PaymentResponse> getPaymentsByCustomer(
            Long customerId,
            PageRequestDto pageRequest) {

        Pageable pageable =
                PageResponseBuilder.buildPageable(pageRequest);

        Page<Payment> payments =
                paymentRepository.findByCustomerId(
                        customerId,
                        pageable);

        return PageResponseBuilder.build(
                payments,
                paymentMapper::toResponse);
    }

    @Override
    @Transactional
    public PageResponse<PaymentResponse> getPaymentsBySale(
            Long saleId,
            PageRequestDto pageRequest) {

        Pageable pageable =
                PageResponseBuilder.buildPageable(pageRequest);

        Page<Payment> payments =
                paymentRepository.findBySaleId(
                        saleId,
                        pageable);

        return PageResponseBuilder.build(
                payments,
                paymentMapper::toResponse);
    }
}