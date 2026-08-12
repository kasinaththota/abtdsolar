package com.abtd.solarbackend.report.service;

import com.abtd.solarbackend.report.dto.response.InventoryReportResponse;
import com.abtd.solarbackend.report.dto.response.ProfitReportResponse;
import com.abtd.solarbackend.report.dto.response.PurchaseReportResponse;
import com.abtd.solarbackend.report.dto.response.SalesReportResponse;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {

    SalesReportResponse getSalesReport(
            LocalDate fromDate,
            LocalDate toDate);

    PurchaseReportResponse getPurchaseReport(
            LocalDate fromDate,
            LocalDate toDate);

    List<InventoryReportResponse> getInventoryReport();

    ProfitReportResponse getProfitReport();

}