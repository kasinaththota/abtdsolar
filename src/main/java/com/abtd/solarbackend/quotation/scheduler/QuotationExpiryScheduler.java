package com.abtd.solarbackend.quotation.scheduler;

import com.abtd.solarbackend.enums.QuotationStatus;
import com.abtd.solarbackend.quotation.entity.Quotation;
import com.abtd.solarbackend.quotation.repository.QuotationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuotationExpiryScheduler {

    private final QuotationRepository quotationRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void expireQuotations() {

        int updated = quotationRepository.expireQuotations(
                QuotationStatus.APPROVED,
                QuotationStatus.EXPIRED,
                LocalDate.now());

        log.info("{} quotation(s) expired.", updated);
    }
}