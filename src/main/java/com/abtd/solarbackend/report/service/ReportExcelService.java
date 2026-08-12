package com.abtd.solarbackend.report.service;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;

public interface ReportExcelService {

    ByteArrayInputStream exportSalesReport(
            LocalDate fromDate,
            LocalDate toDate);

    ByteArrayInputStream exportPurchaseReport(
            LocalDate fromDate,
            LocalDate toDate);

    ByteArrayInputStream exportInventoryReport();

    ByteArrayInputStream exportProfitReport();
}