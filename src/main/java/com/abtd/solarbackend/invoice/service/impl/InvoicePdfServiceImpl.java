package com.abtd.solarbackend.invoice.service.impl;

import com.abtd.solarbackend.invoice.entity.Invoice;
import com.abtd.solarbackend.invoice.exception.InvoiceNotFoundException;
import com.abtd.solarbackend.invoice.repository.InvoiceRepository;
import com.abtd.solarbackend.invoice.service.InvoicePdfService;
import com.abtd.solarbackend.sales.entity.Sale;
import com.abtd.solarbackend.sales.entity.SaleItem;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class InvoicePdfServiceImpl implements InvoicePdfService {

    private final InvoiceRepository invoiceRepository;

    @Override
    public byte[] generateInvoice(Long invoiceId) {

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() ->
                        new InvoiceNotFoundException(invoiceId));

        Sale sale = invoice.getSale();

        try {

            ByteArrayOutputStream outputStream =
                    new ByteArrayOutputStream();

            Document document =
                    new Document(PageSize.A4, 30, 30, 30, 30);

            PdfWriter.getInstance(document, outputStream);

            document.open();

            addCompanyHeader(document);

            addInvoiceInfo(document, invoice);

            addCustomerDetails(document, sale);

            addItemsTable(document, sale);

            addTotals(document, sale);

            addPaymentDetails(document, sale);

            addTerms(document);

            addSignature(document);

            document.close();

            return outputStream.toByteArray();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to generate invoice PDF", e);
        }
    }

    private void addCompanyHeader(Document document)
            throws Exception {

        Font title =
                new Font(Font.HELVETICA, 20, Font.BOLD);

        Font normal =
                new Font(Font.HELVETICA, 11);

        Paragraph company =
                new Paragraph("ARAVINDH B SOLAR", title);

        company.setAlignment(Element.ALIGN_CENTER);

        document.add(company);

        Paragraph address =
                new Paragraph(
                        "Hyderabad\n"
                                + "Phone : +91-XXXXXXXXXX\n"
                                + "Email : info@aravindhsolar.com\n"
                                + "GSTIN : 36XXXXXXXXXXXX",
                        normal);

        address.setAlignment(Element.ALIGN_CENTER);

        document.add(address);

        document.add(new Paragraph(" "));
    }

    private void addInvoiceInfo(
            Document document,
            Invoice invoice)
            throws Exception {

        PdfPTable table =
                new PdfPTable(2);

        table.setWidthPercentage(100);

        table.addCell(getLabelCell("Invoice No"));

        table.addCell(getValueCell(
                invoice.getInvoiceNumber()));

        table.addCell(getLabelCell("Invoice Date"));

        table.addCell(getValueCell(
                invoice.getInvoiceDate().toString()));

        table.addCell(getLabelCell("Due Date"));

        table.addCell(getValueCell(
                invoice.getDueDate().toString()));

        document.add(table);

        document.add(new Paragraph(" "));
    }

    private void addCustomerDetails(
            Document document,
            Sale sale)
            throws Exception {

        Font heading =
                new Font(Font.HELVETICA, 14, Font.BOLD);

        document.add(
                new Paragraph("Customer Details", heading));

        document.add(new Paragraph(

                "Name : "
                        + sale.getCustomer().getFirstName()
                        + " "
                        + sale.getCustomer().getLastName()

        ));

        document.add(new Paragraph(

                "Phone : "
                        + sale.getCustomer().getMobile()

        ));

        document.add(new Paragraph(

                "Email : "
                        + sale.getCustomer().getEmail()

        ));

        document.add(new Paragraph(" "));
    }

    private void addItemsTable(
            Document document,
            Sale sale)
            throws Exception {

        PdfPTable table =
                new PdfPTable(6);

        table.setWidthPercentage(100);

        table.setWidths(
                new float[]{1, 4, 2, 2, 2, 2});

        table.addCell(header("S.No"));
        table.addCell(header("Product"));
        table.addCell(header("Qty"));
        table.addCell(header("Price"));
        table.addCell(header("GST"));
        table.addCell(header("Total"));

        int count = 1;

        for (SaleItem item : sale.getSaleItems()) {

            table.addCell(value(String.valueOf(count++)));

            table.addCell(value(
                    item.getProduct().getName()));

            table.addCell(value(
                    item.getQuantity().toString()));

            table.addCell(value(
                    item.getUnitPrice().toString()));

            table.addCell(value(
                    item.getGstPercentage().toString() + "%"));

            table.addCell(value(
                    item.getTotalPrice().toString()));
        }

        document.add(table);

        document.add(new Paragraph(" "));
    }

    private void addTotals(
            Document document,
            Sale sale)
            throws Exception {

        PdfPTable table = new PdfPTable(2);

        table.setWidthPercentage(45);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);

        table.addCell(getLabelCell("Subtotal"));
        table.addCell(getValueCell(sale.getSubtotal().toString()));

        table.addCell(getLabelCell("GST"));
        table.addCell(getValueCell(sale.getGstAmount().toString()));

        table.addCell(getLabelCell("Discount"));
        table.addCell(getValueCell(sale.getDiscountAmount().toString()));

        PdfPCell totalLabel = new PdfPCell(
                new Phrase(
                        "Grand Total",
                        new Font(Font.HELVETICA, 12, Font.BOLD)));

        PdfPCell totalValue = new PdfPCell(
                new Phrase(
                        sale.getTotalAmount().toString(),
                        new Font(Font.HELVETICA, 12, Font.BOLD)));

        table.addCell(totalLabel);
        table.addCell(totalValue);

        document.add(table);

        document.add(new Paragraph(" "));
    }

    private void addPaymentDetails(
            Document document,
            Sale sale)
            throws Exception {

        Font heading =
                new Font(Font.HELVETICA, 14, Font.BOLD);

        document.add(new Paragraph(
                "Payment Details",
                heading));

        PdfPTable table = new PdfPTable(2);

        table.setWidthPercentage(100);

        table.addCell(getLabelCell("Paid Amount"));
        table.addCell(getValueCell(
                sale.getPaidAmount().toString()));

        table.addCell(getLabelCell("Balance Amount"));
        table.addCell(getValueCell(
                sale.getBalanceAmount().toString()));

        table.addCell(getLabelCell("Payment Status"));
        table.addCell(getValueCell(
                sale.getPaymentStatus().name()));

        document.add(table);

        document.add(new Paragraph(" "));
    }

    private void addTerms(
            Document document)
            throws Exception {

        Font heading =
                new Font(Font.HELVETICA, 14, Font.BOLD);

        document.add(new Paragraph(
                "Terms & Conditions",
                heading));

        document.add(new Paragraph(
                "1. Goods once sold will not be taken back."));

        document.add(new Paragraph(
                "2. Warranty is applicable as per manufacturer policy."));

        document.add(new Paragraph(
                "3. Payment should be completed before due date."));

        document.add(new Paragraph(
                "4. Subject to Hyderabad jurisdiction."));

        document.add(new Paragraph(" "));
    }

    private void addSignature(
            Document document)
            throws Exception {

        Paragraph p = new Paragraph();

        p.setAlignment(Element.ALIGN_RIGHT);

        p.add(new Phrase(
                "\n\n\nAuthorized Signature\n",
                new Font(Font.HELVETICA, 12, Font.BOLD)));

        p.add(new Phrase("ARAVINDH B SOLAR"));

        document.add(p);
    }

    private PdfPCell header(String text) {

        PdfPCell cell = new PdfPCell(
                new Phrase(
                        text,
                        new Font(Font.HELVETICA, 11, Font.BOLD)));

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        cell.setPadding(6);

        return cell;
    }

    private PdfPCell value(String text) {

        PdfPCell cell = new PdfPCell(
                new Phrase(
                        text,
                        new Font(Font.HELVETICA, 10)));

        cell.setPadding(5);

        return cell;
    }

    private PdfPCell getLabelCell(String text) {

        PdfPCell cell = new PdfPCell(
                new Phrase(
                        text,
                        new Font(Font.HELVETICA, 11, Font.BOLD)));

        cell.setPadding(5);

        return cell;
    }

    private PdfPCell getValueCell(String text) {

        PdfPCell cell = new PdfPCell(
                new Phrase(
                        text,
                        new Font(Font.HELVETICA, 11)));

        cell.setPadding(5);

        return cell;
    }
}