package com.abtd.solarbackend.quotation.controller;

import com.abtd.solarbackend.common.dto.PageRequestDto;
import com.abtd.solarbackend.common.response.ResponseBuilder;
import com.abtd.solarbackend.quotation.dto.request.CreateQuotationRequest;
import com.abtd.solarbackend.quotation.dto.request.QuotationSearchRequest;
import com.abtd.solarbackend.quotation.dto.request.UpdateQuotationRequest;
import com.abtd.solarbackend.quotation.pdf.QuotationPdfService;
import com.abtd.solarbackend.quotation.service.QuotationEmailService;
import com.abtd.solarbackend.quotation.service.QuotationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/quotations")
@RequiredArgsConstructor
public class QuotationController {

    private final QuotationService quotationService;
    private final QuotationPdfService quotationPdfService;
    private final QuotationEmailService quotationEmailService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SALES')")
    public ResponseEntity<?> createQuotation(
            @Valid @RequestBody CreateQuotationRequest request) {

        return ResponseBuilder.created(
                "Quotation created successfully",
                quotationService.createQuotation(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SALES')")
    public ResponseEntity<?> getQuotationById(
            @PathVariable Long id) {

        return ResponseBuilder.ok(
                "Quotation fetched successfully",
                quotationService.getQuotationById(id));
    }

    @PostMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN','SALES')")
    public ResponseEntity<?> getAllQuotations(
            @RequestBody PageRequestDto request) {

        return ResponseBuilder.ok(
                "Quotations fetched successfully",
                quotationService.getAllQuotations(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SALES')")
    public ResponseEntity<?> updateQuotation(
            @PathVariable Long id,
            @Valid @RequestBody UpdateQuotationRequest request) {

        return ResponseBuilder.ok(
                "Quotation updated successfully",
                quotationService.updateQuotation(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteQuotation(
            @PathVariable Long id) {

        quotationService.deleteQuotation(id);

        return ResponseBuilder.ok(
                "Quotation deleted successfully",
                null);
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN','SALES')")
    public ResponseEntity<?> approveQuotation(
            @PathVariable Long id) {

        return ResponseBuilder.ok(
                "Quotation approved successfully",
                quotationService.approveQuotation(id));
    }

    @PostMapping("/{id}/convert-sale")
    @PreAuthorize("hasAnyRole('ADMIN','SALES')")
    public ResponseEntity<?> convertToSale(
            @PathVariable Long id) {

        Long saleId = quotationService.convertToSale(id);

        return ResponseBuilder.ok(
                "Quotation converted to Sale successfully",
                saleId);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<InputStreamResource> downloadQuotationPdf(
            @PathVariable Long id) {

        ByteArrayInputStream pdf =
                quotationPdfService.generateQuotationPdf(id);

        HttpHeaders headers = new HttpHeaders();

        headers.add(
                "Content-Disposition",
                "inline; filename=quotation.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN','SALES')")
    public ResponseEntity<?> dashboard() {

        return ResponseBuilder.ok(
                "Quotation dashboard fetched successfully",
                quotationService.getDashboard());
    }

    @PostMapping("/{id}/email")
    @PreAuthorize("hasAnyRole('ADMIN','SALES')")
    public ResponseEntity<?> emailQuotation(
            @PathVariable Long id) {

        quotationEmailService.emailQuotation(id);

        return ResponseBuilder.ok(
                "Quotation emailed successfully.",
                null);
    }

    @PostMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','SALES')")
    public ResponseEntity<?> searchQuotations(

            @RequestBody
            QuotationSearchRequest request) {

        return ResponseBuilder.ok(

                "Quotations fetched successfully",

                quotationService.searchQuotations(request));
    }
}