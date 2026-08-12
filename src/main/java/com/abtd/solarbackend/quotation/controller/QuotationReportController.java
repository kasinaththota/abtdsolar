package com.abtd.solarbackend.quotation.controller;

import com.abtd.solarbackend.quotation.report.QuotationReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/quotation-reports")
@RequiredArgsConstructor
public class QuotationReportController {

    private final QuotationReportService reportService;

    @GetMapping("/excel")
    public ResponseEntity<InputStreamResource> exportExcel() {

        ByteArrayInputStream stream =
                reportService.exportQuotationExcel();

        HttpHeaders headers =
                new HttpHeaders();

        headers.add(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=quotations.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(stream));
    }
}