package com.abtd.solarbackend.invoice.controller;

import com.abtd.solarbackend.common.constants.InvoiceMessages;
import com.abtd.solarbackend.common.dto.PageRequestDto;
import com.abtd.solarbackend.common.dto.PageResponse;
import com.abtd.solarbackend.common.response.ApiResponse;
import com.abtd.solarbackend.common.response.ResponseBuilder;
import com.abtd.solarbackend.invoice.dto.request.CreateInvoiceRequest;
import com.abtd.solarbackend.invoice.dto.response.InvoiceResponse;
import com.abtd.solarbackend.invoice.service.InvoicePdfService;
import com.abtd.solarbackend.invoice.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','ACCOUNTANT')")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final InvoicePdfService invoicePdfService;

    @PostMapping
    public ResponseEntity<ApiResponse<InvoiceResponse>> createInvoice(
            @Valid @RequestBody CreateInvoiceRequest request) {

        return ResponseBuilder.created(
                InvoiceMessages.INVOICE_CREATED,
                invoiceService.createInvoice(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InvoiceResponse>> getInvoiceById(
            @PathVariable Long id) {

        return ResponseBuilder.ok(
                InvoiceMessages.INVOICE_FETCHED,
                invoiceService.getInvoiceById(id));
    }

    @GetMapping("/sale/{saleId}")
    public ResponseEntity<ApiResponse<InvoiceResponse>> getInvoiceBySale(
            @PathVariable Long saleId) {

        return ResponseBuilder.ok(
                InvoiceMessages.INVOICE_FETCHED,
                invoiceService.getInvoiceBySale(saleId));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<InvoiceResponse>>> getAllInvoices(
            @ModelAttribute PageRequestDto pageRequest) {

        return ResponseBuilder.ok(
                InvoiceMessages.INVOICES_FETCHED,
                invoiceService.getAllInvoices(pageRequest));
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable Long id) {

        byte[] pdf = invoicePdfService.generateInvoice(id);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=invoice.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}