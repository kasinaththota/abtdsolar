package com.abtd.solarbackend.invoice.repository;

import com.abtd.solarbackend.invoice.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    Optional<Invoice> findBySaleId(Long saleId);

    Optional<Invoice> findTopByOrderByIdDesc();

    boolean existsBySaleId(Long saleId);

    // ================= Dashboard =================

    long countByPaidTrue();

    long countByPaidFalse();

}