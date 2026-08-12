package com.abtd.solarbackend.purchase.repository;

import com.abtd.solarbackend.enums.PurchaseStatus;
import com.abtd.solarbackend.purchase.entity.Purchase;
import com.abtd.solarbackend.vendor.entity.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    Optional<Purchase> findByPurchaseNumber(String purchaseNumber);

    boolean existsByPurchaseNumber(String purchaseNumber);

    Page<Purchase> findByStatus(
            PurchaseStatus status,
            Pageable pageable);

    Page<Purchase> findByVendor(
            Vendor vendor,
            Pageable pageable);

    Page<Purchase> findByVendorId(
            Long vendorId,
            Pageable pageable);

    Page<Purchase> findByPurchaseDateBetween(
            LocalDate fromDate,
            LocalDate toDate,
            Pageable pageable);

    Optional<Purchase> findTopByOrderByIdDesc();

    // ================= Dashboard =================

    @Query("""
            SELECT COALESCE(SUM(p.totalAmount), 0)
            FROM Purchase p
            """)
    BigDecimal getTotalPurchaseAmount();

    @Query("""
            SELECT COUNT(p)
            FROM Purchase p
            WHERE p.purchaseDate BETWEEN :fromDate AND :toDate
            """)
    long countPurchasesBetween(LocalDate fromDate, LocalDate toDate);

    @Query("""
            SELECT COALESCE(SUM(p.totalAmount), 0)
            FROM Purchase p
            WHERE p.purchaseDate BETWEEN :fromDate AND :toDate
            """)
    BigDecimal getPurchaseAmountBetween(LocalDate fromDate, LocalDate toDate);

    @Query("""
            SELECT COALESCE(SUM(p.gstAmount), 0)
            FROM Purchase p
            WHERE p.purchaseDate BETWEEN :fromDate AND :toDate
            """)
    BigDecimal getPurchaseGstBetween(LocalDate fromDate, LocalDate toDate);

    @Query("""
            SELECT p
            FROM Purchase p
            WHERE p.purchaseDate BETWEEN :fromDate AND :toDate
            ORDER BY p.purchaseDate
            """)
    List<Purchase> findPurchasesBetween(
            LocalDate fromDate,
            LocalDate toDate);

}