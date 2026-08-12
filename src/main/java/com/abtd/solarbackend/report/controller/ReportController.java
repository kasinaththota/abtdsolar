package com.abtd.solarbackend.report.controller;

import com.abtd.solarbackend.common.constants.ReportMessages;
import com.abtd.solarbackend.common.response.ResponseBuilder;
import com.abtd.solarbackend.report.service.ReportExcelService;
import com.abtd.solarbackend.report.service.ReportPdfService;
import com.abtd.solarbackend.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
public class ReportController {

    private final ReportService reportService;
    private final ReportExcelService reportExcelService;
    private final ReportPdfService reportPdfService;

    @GetMapping("/sales")
    public ResponseEntity<?> getSalesReport(
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate) {

        return ResponseBuilder.ok(
                ReportMessages.SALES_REPORT_FETCHED,
                reportService.getSalesReport(fromDate, toDate)
        );
    }

    @GetMapping("/purchases")
    public ResponseEntity<?> getPurchaseReport(
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate) {

        return ResponseBuilder.ok(
                ReportMessages.PURCHASE_REPORT_FETCHED,
                reportService.getPurchaseReport(fromDate, toDate)
        );
    }

    @GetMapping("/inventory")
    public ResponseEntity<?> getInventoryReport() {

        return ResponseBuilder.ok(
                ReportMessages.INVENTORY_REPORT_FETCHED,
                reportService.getInventoryReport()
        );
    }

    @GetMapping("/profit")
    public ResponseEntity<?> getProfitReport() {

        return ResponseBuilder.ok(
                ReportMessages.PROFIT_REPORT_FETCHED,
                reportService.getProfitReport()
        );
    }

    @GetMapping("/sales/pdf")
    public ResponseEntity<InputStreamResource> exportSalesPdf(
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate) {


        ByteArrayInputStream pdf =
                reportPdfService.exportSalesReport(fromDate, toDate);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=sales-report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }

    @GetMapping("/sales/excel")
    public ResponseEntity<InputStreamResource> exportSalesExcel(
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate) {

        ByteArrayInputStream excel =
                reportExcelService.exportSalesReport(fromDate, toDate);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=sales-report.xlsx")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(excel));
    }

    @GetMapping("/purchases/pdf")
    public ResponseEntity<InputStreamResource> exportPurchasePdf(
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate) {

        ByteArrayInputStream pdf =
                reportPdfService.exportPurchaseReport(fromDate, toDate);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=purchase-report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }

    @GetMapping("/purchases/excel")
    public ResponseEntity<InputStreamResource> exportPurchaseExcel(
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate) {

        ByteArrayInputStream excel =
                reportExcelService.exportPurchaseReport(fromDate, toDate);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=purchase-report.xlsx")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(excel));
    }

    @GetMapping("/profit/pdf")
    public ResponseEntity<InputStreamResource> exportProfitPdf() {

        ByteArrayInputStream pdf =
                reportPdfService.exportProfitReport();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=profit-report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }

    @GetMapping("/profit/excel")
    public ResponseEntity<InputStreamResource> exportProfitExcel() {

        ByteArrayInputStream excel =
                reportExcelService.exportProfitReport();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=profit-report.xlsx")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(excel));
    }
}