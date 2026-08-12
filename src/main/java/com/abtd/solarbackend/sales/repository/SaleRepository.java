package com.abtd.solarbackend.sales.repository;

import com.abtd.solarbackend.enums.SalesStatus;
import com.abtd.solarbackend.sales.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    Optional<Sale> findBySaleNumber(String saleNumber);

    Optional<Sale> findTopByOrderByIdDesc();

    Page<Sale> findByStatus(
            SalesStatus status,
            Pageable pageable);

    Page<Sale> findByCustomerId(
            Long customerId,
            Pageable pageable);

    Page<Sale> findBySaleDateBetween(
            LocalDate fromDate,
            LocalDate toDate,
            Pageable pageable);

    @Query("""
            SELECT COALESCE(SUM(s.totalAmount), 0)
            FROM Sale s
            """)
    BigDecimal getTotalSalesAmount();

    @Query("""
            SELECT COALESCE(SUM(s.balanceAmount), 0)
            FROM Sale s
            WHERE s.balanceAmount > 0
            """)
    BigDecimal getTotalPendingPayments();

    @Query("""
            SELECT COUNT(s)
            FROM Sale s
            WHERE s.saleDate BETWEEN :fromDate AND :toDate
            """)
    long countSalesBetween(LocalDate fromDate, LocalDate toDate);

    @Query("""
            SELECT COALESCE(SUM(s.totalAmount), 0)
            FROM Sale s
            WHERE s.saleDate BETWEEN :fromDate AND :toDate
            """)
    BigDecimal getSalesAmountBetween(LocalDate fromDate, LocalDate toDate);

    @Query("""
            SELECT COALESCE(SUM(s.gstAmount), 0)
            FROM Sale s
            WHERE s.saleDate BETWEEN :fromDate AND :toDate
            """)
    BigDecimal getGstAmountBetween(LocalDate fromDate, LocalDate toDate);

    @Query("""
            SELECT COALESCE(SUM(s.discountAmount), 0)
            FROM Sale s
            WHERE s.saleDate BETWEEN :fromDate AND :toDate
            """)
    BigDecimal getDiscountAmountBetween(LocalDate fromDate, LocalDate toDate);

    @Query("""
            SELECT s
            FROM Sale s
            WHERE s.saleDate BETWEEN :fromDate AND :toDate
            ORDER BY s.saleDate ASC
            """)
    List<Sale> findSalesBetween(
            LocalDate fromDate,
            LocalDate toDate);

    
}