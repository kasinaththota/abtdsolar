package com.abtd.solarbackend.quotation.service.impl;

import com.abtd.solarbackend.exception.ResourceNotFoundException;
import com.abtd.solarbackend.mail.service.EmailService;
import com.abtd.solarbackend.quotation.entity.Quotation;
import com.abtd.solarbackend.quotation.pdf.QuotationPdfService;
import com.abtd.solarbackend.quotation.repository.QuotationRepository;
import com.abtd.solarbackend.quotation.service.QuotationEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
@RequiredArgsConstructor
public class QuotationEmailServiceImpl
        implements QuotationEmailService {

    private final QuotationRepository quotationRepository;
    private final QuotationPdfService quotationPdfService;
    private final EmailService emailService;

    @Override
    public void emailQuotation(Long quotationId) {

        Quotation quotation = quotationRepository.findById(quotationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Quotation not found with id : " + quotationId));

        String email = quotation.getCustomer().getEmail();

        if (email == null || email.isBlank()) {
            throw new RuntimeException(
                    "Customer email is not available.");
        }

        ByteArrayInputStream inputStream =
                quotationPdfService.generateQuotationPdf(
                        quotationId);

        byte[] pdfBytes;

        try {

            pdfBytes = inputStream.readAllBytes();

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Unable to generate quotation PDF.",
                    ex);
        }

        String subject =
                "Quotation - " + quotation.getQuotationNumber();

        String body =
                """
                        Dear %s,
                        
                        Thank you for choosing ARAVINDH B SOLAR.
                        
                        Please find your quotation attached.
                        
                        Quotation Number : %s
                        
                        Total Amount : %s
                        
                        Regards,
                        ARAVINDH B SOLAR
                        """
                        .formatted(
                                quotation.getCustomer().getFirstName(),
                                quotation.getQuotationNumber(),
                                quotation.getTotalAmount());

        emailService.sendEmailWithAttachment(
                email,
                subject,
                body,
                pdfBytes,
                quotation.getQuotationNumber() + ".pdf");
    }
}