package com.abtd.solarbackend.quotation.pdf;

import com.abtd.solarbackend.quotation.entity.Quotation;
import com.abtd.solarbackend.quotation.entity.QuotationItem;
import com.abtd.solarbackend.quotation.repository.QuotationRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class QuotationPdfServiceImpl
        implements QuotationPdfService {

    private final QuotationRepository quotationRepository;

    @Override
    public ByteArrayInputStream generateQuotationPdf(
            Long quotationId) {

        Quotation quotation =
                quotationRepository.findById(quotationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Quotation not found"));

        ByteArrayOutputStream out =
                new ByteArrayOutputStream();

        Document document =
                new Document(PageSize.A4);

        PdfWriter.getInstance(document, out);

        document.open();

        Font titleFont =
                FontFactory.getFont(
                        FontFactory.HELVETICA_BOLD,
                        18);

        Paragraph title =
                new Paragraph(
                        "ARAVINDH B SOLAR",
                        titleFont);

        title.setAlignment(Element.ALIGN_CENTER);

        document.add(title);

        document.add(
                new Paragraph(
                        "SOLAR QUOTATION"));

        document.add(
                new Paragraph(" "));

        document.add(new Paragraph(
                "Quotation No : "
                        + quotation.getQuotationNumber()));

        document.add(new Paragraph(
                "Customer : "
                        + quotation.getCustomer().getFirstName()
                        + " "
                        + quotation.getCustomer().getLastName()));

        document.add(new Paragraph(
                "Date : "
                        + quotation.getQuotationDate()));

        document.add(new Paragraph(
                "Valid Till : "
                        + quotation.getValidTill()));

        document.add(new Paragraph(" "));

        PdfPTable table =
                new PdfPTable(5);

        table.setWidthPercentage(100);

        table.addCell("Product");

        table.addCell("Qty");

        table.addCell("Price");

        table.addCell("GST");

        table.addCell("Total");

        for (QuotationItem item :
                quotation.getQuotationItems()) {

            table.addCell(
                    item.getProduct().getName());

            table.addCell(
                    item.getQuantity().toString());

            table.addCell(
                    item.getPrice().toString());

            table.addCell(
                    item.getGstPercentage().toString());

            table.addCell(
                    item.getLineTotal().toString());
        }

        document.add(table);

        document.add(new Paragraph(" "));

        document.add(new Paragraph(
                "Subtotal : "
                        + quotation.getSubtotal()));

        document.add(new Paragraph(
                "GST : "
                        + quotation.getGstAmount()));

        document.add(new Paragraph(
                "Discount : "
                        + quotation.getDiscountAmount()));

        document.add(new Paragraph(
                "Grand Total : "
                        + quotation.getTotalAmount()));

        document.add(new Paragraph(" "));

        document.add(new Paragraph(
                "Terms & Conditions"));

        document.add(new Paragraph(
                "1. Prices are inclusive of applicable GST."));

        document.add(new Paragraph(
                "2. Quotation valid till "
                        + quotation.getValidTill()));

        document.add(new Paragraph(
                "3. Thank you for choosing ARAVINDH B SOLAR."));

        document.close();

        return new ByteArrayInputStream(
                out.toByteArray());
    }
}