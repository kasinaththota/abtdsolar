package com.abtd.solarbackend.payment.repository;

import com.abtd.solarbackend.customer.entity.Customer;
import com.abtd.solarbackend.enums.PaymentStatus;
import com.abtd.solarbackend.payment.entity.Payment;
import com.abtd.solarbackend.sales.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByPaymentNumber(String paymentNumber);

    Optional<Payment> findTopByOrderByIdDesc();

    Page<Payment> findByCustomerId(
            Long customerId,
            Pageable pageable);

    Page<Payment> findBySaleId(
            Long saleId,
            Pageable pageable);

    Page<Payment> findByPaymentStatus(
            PaymentStatus paymentStatus,
            Pageable pageable);

    Page<Payment> findByPaymentDateBetween(
            LocalDate fromDate,
            LocalDate toDate,
            Pageable pageable);

    // ================= Dashboard =================

    @Query("""
            SELECT COALESCE(SUM(p.amount), 0)
            FROM Payment p
            """)
    BigDecimal getTotalPaymentsReceived();

}