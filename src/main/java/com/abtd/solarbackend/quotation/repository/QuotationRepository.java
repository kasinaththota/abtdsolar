package com.abtd.solarbackend.quotation.repository;

import com.abtd.solarbackend.customer.entity.Customer;
import com.abtd.solarbackend.enums.QuotationStatus;
import com.abtd.solarbackend.quotation.entity.Quotation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface QuotationRepository extends
        JpaRepository<Quotation, Long>,
        JpaSpecificationExecutor<Quotation> {

    Optional<Quotation> findTopByOrderByIdDesc();

    Page<Quotation> findByStatus(
            QuotationStatus status,
            Pageable pageable);

    Page<Quotation> findByCustomer(
            Customer customer,
            Pageable pageable);

    List<Quotation> findByQuotationDateBetween(
            LocalDate fromDate,
            LocalDate toDate);

    List<Quotation> findByStatus(
            QuotationStatus status);

    long countByStatus(
            QuotationStatus status);

    List<Quotation> findByStatusAndValidTillBefore(
            QuotationStatus status,
            LocalDate date);

    @Query("""
            SELECT COALESCE(SUM(q.totalAmount),0)
            FROM Quotation q
            """)
    BigDecimal getTotalQuotationAmount();

    @Modifying
    @Query("""
            UPDATE Quotation q
            SET q.status = :expiredStatus
            WHERE q.status = :approvedStatus
            AND q.validTill < :today
            """)
    int expireQuotations(
            @Param("approvedStatus") QuotationStatus approvedStatus,
            @Param("expiredStatus") QuotationStatus expiredStatus,
            @Param("today") LocalDate today);
}