package com.abtd.solarbackend.quotation.repository;

import com.abtd.solarbackend.quotation.entity.Quotation;
import com.abtd.solarbackend.quotation.entity.QuotationItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuotationItemRepository
        extends JpaRepository<QuotationItem, Long> {

    List<QuotationItem> findByQuotation(
            Quotation quotation);

    void deleteByQuotation(
            Quotation quotation);
}