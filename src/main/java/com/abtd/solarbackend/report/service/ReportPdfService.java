package com.abtd.solarbackend.report.service;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;

public interface ReportPdfService {

    ByteArrayInputStream exportSalesReport(
            LocalDate fromDate,
            LocalDate toDate);

    ByteArrayInputStream exportPurchaseReport(
            LocalDate fromDate,
            LocalDate toDate);

    ByteArrayInputStream exportProfitReport();
}