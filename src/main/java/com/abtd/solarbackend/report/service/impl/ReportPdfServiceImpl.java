package com.abtd.solarbackend.report.service.impl;

import com.abtd.solarbackend.purchase.entity.Purchase;
import com.abtd.solarbackend.purchase.repository.PurchaseRepository;
import com.abtd.solarbackend.report.service.ReportPdfService;
import com.abtd.solarbackend.sales.entity.Sale;
import com.abtd.solarbackend.sales.repository.SaleRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportPdfServiceImpl implements ReportPdfService {

    private final SaleRepository saleRepository;
    private final PurchaseRepository purchaseRepository;

    @Override
    public ByteArrayInputStream exportSalesReport(LocalDate fromDate,
                                                  LocalDate toDate) {

        List<Sale> sales = saleRepository.findSalesBetween(fromDate, toDate);

        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfWriter.getInstance(document, out);

            document.open();

            addHeader(document, fromDate, toDate);

            addSalesTable(document, sales);

            addSummary(document, sales);

            document.close();

        } catch (Exception ex) {
            throw new RuntimeException("Failed to generate Sales Report PDF", ex);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addHeader(Document document,
                           LocalDate fromDate,
                           LocalDate toDate) throws Exception {

        Font companyFont = FontFactory.getFont(
                FontFactory.HELVETICA_BOLD, 18);

        Font titleFont = FontFactory.getFont(
                FontFactory.HELVETICA_BOLD, 15);

        Font normalFont = FontFactory.getFont(
                FontFactory.HELVETICA, 11);

        Paragraph company =
                new Paragraph("ARAVINDH B SOLAR", companyFont);
        company.setAlignment(Element.ALIGN_CENTER);

        Paragraph title =
                new Paragraph("SALES REPORT", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);

        document.add(company);
        document.add(title);
        document.add(new Paragraph(" "));

        document.add(new Paragraph(
                "From Date : " + fromDate,
                normalFont));

        document.add(new Paragraph(
                "To Date : " + toDate,
                normalFont));

        document.add(new Paragraph(
                "Generated On : " +
                        LocalDateTime.now()
                                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                normalFont));

        document.add(new Paragraph(" "));
    }

    private void addSalesTable(Document document,
                               List<Sale> sales) throws Exception {

        PdfPTable table = new PdfPTable(6);

        table.setWidthPercentage(100);

        table.setSpacingBefore(10);

        table.setWidths(new float[]{
                2.5f,
                2.5f,
                4f,
                2.5f,
                2.5f,
                2.5f
        });

        addHeaderCell(table, "Sale No");
        addHeaderCell(table, "Sale Date");
        addHeaderCell(table, "Customer");
        addHeaderCell(table, "Total");
        addHeaderCell(table, "Paid");
        addHeaderCell(table, "Balance");

        for (Sale sale : sales) {

            table.addCell(getValue(sale.getSaleNumber()));

            table.addCell(
                    sale.getSaleDate() == null
                            ? ""
                            : sale.getSaleDate().toString());

            table.addCell(
                    sale.getCustomer() == null
                            ? ""
                            : getValue(
                            sale.getCustomer() != null
                                    ? sale.getCustomer().getFirstName() + " " + sale.getCustomer().getLastName()
                                    : ""
                    ));

            table.addCell(formatAmount(sale.getTotalAmount()));

            table.addCell(formatAmount(sale.getPaidAmount()));

            table.addCell(formatAmount(sale.getBalanceAmount()));
        }

        document.add(table);
    }

    private void addSummary(Document document,
                            List<Sale> sales) throws Exception {

        BigDecimal grandTotal = BigDecimal.ZERO;
        BigDecimal totalPaid = BigDecimal.ZERO;
        BigDecimal totalBalance = BigDecimal.ZERO;

        for (Sale sale : sales) {

            if (sale.getTotalAmount() != null) {
                grandTotal = grandTotal.add(sale.getTotalAmount());
            }

            if (sale.getPaidAmount() != null) {
                totalPaid = totalPaid.add(sale.getPaidAmount());
            }

            if (sale.getBalanceAmount() != null) {
                totalBalance = totalBalance.add(sale.getBalanceAmount());
            }
        }

        document.add(new Paragraph(" "));

        Font bold = FontFactory.getFont(
                FontFactory.HELVETICA_BOLD, 12);

        document.add(new Paragraph(
                "Total Sales : " + sales.size(), bold));

        document.add(new Paragraph(
                "Grand Total : ₹ " + grandTotal, bold));

        document.add(new Paragraph(
                "Total Paid : ₹ " + totalPaid, bold));

        document.add(new Paragraph(
                "Pending Amount : ₹ " + totalBalance, bold));
    }

    private void addHeaderCell(PdfPTable table,
                               String text) {

        PdfPCell cell = new PdfPCell(new Paragraph(
                text,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11)));

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(8);

        table.addCell(cell);
    }

    private String formatAmount(BigDecimal value) {

        return value == null
                ? "0.00"
                : value.setScale(2).toString();
    }

    private String getValue(String value) {

        return value == null ? "" : value;
    }

    @Override
    public ByteArrayInputStream exportPurchaseReport(LocalDate fromDate,
                                                     LocalDate toDate) {

        List<Purchase> purchases =
                purchaseRepository.findPurchasesBetween(fromDate, toDate);

        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfWriter.getInstance(document, out);

            document.open();

            addPurchaseHeader(document, fromDate, toDate);

            addPurchaseTable(document, purchases);

            addPurchaseSummary(document, purchases);

            document.close();

        } catch (Exception ex) {
            throw new RuntimeException(
                    "Failed to generate Purchase Report PDF",
                    ex);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addPurchaseHeader(Document document,
                                   LocalDate fromDate,
                                   LocalDate toDate) throws Exception {

        Font companyFont =
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);

        Font titleFont =
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15);

        Font normalFont =
                FontFactory.getFont(FontFactory.HELVETICA, 11);

        Paragraph company =
                new Paragraph("ARAVINDH B SOLAR", companyFont);
        company.setAlignment(Element.ALIGN_CENTER);

        Paragraph title =
                new Paragraph("PURCHASE REPORT", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);

        document.add(company);
        document.add(title);
        document.add(new Paragraph(" "));

        document.add(new Paragraph(
                "From Date : " + fromDate,
                normalFont));

        document.add(new Paragraph(
                "To Date : " + toDate,
                normalFont));

        document.add(new Paragraph(
                "Generated On : " +
                        LocalDateTime.now().format(
                                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                normalFont));

        document.add(new Paragraph(" "));
    }

    private void addPurchaseTable(Document document,
                                  List<Purchase> purchases) throws Exception {

        PdfPTable table = new PdfPTable(8);

        table.setWidthPercentage(100);
        table.setSpacingBefore(10);

        table.setWidths(new float[]{
                2.5f,
                2.5f,
                4f,
                3f,
                2.5f,
                2.5f,
                2.5f,
                2.5f
        });

        addHeaderCell(table, "Purchase No");
        addHeaderCell(table, "Purchase Date");
        addHeaderCell(table, "Vendor");
        addHeaderCell(table, "Invoice");
        addHeaderCell(table, "Subtotal");
        addHeaderCell(table, "GST");
        addHeaderCell(table, "Discount");
        addHeaderCell(table, "Total");

        for (Purchase purchase : purchases) {

            table.addCell(getValue(purchase.getPurchaseNumber()));

            table.addCell(
                    purchase.getPurchaseDate() == null
                            ? ""
                            : purchase.getPurchaseDate().toString());

            table.addCell(
                    purchase.getVendor() == null
                            ? ""
                            : getValue(purchase.getVendor().getCompanyName()));

            table.addCell(getValue(purchase.getInvoiceNumber()));

            table.addCell(formatAmount(purchase.getSubtotal()));

            table.addCell(formatAmount(purchase.getGstAmount()));

            table.addCell(formatAmount(purchase.getDiscountAmount()));

            table.addCell(formatAmount(purchase.getTotalAmount()));
        }

        document.add(table);
    }

    private void addPurchaseSummary(Document document,
                                    List<Purchase> purchases) throws Exception {

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal gst = BigDecimal.ZERO;
        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;

        for (Purchase purchase : purchases) {

            if (purchase.getSubtotal() != null) {
                subtotal = subtotal.add(purchase.getSubtotal());
            }

            if (purchase.getGstAmount() != null) {
                gst = gst.add(purchase.getGstAmount());
            }

            if (purchase.getDiscountAmount() != null) {
                discount = discount.add(purchase.getDiscountAmount());
            }

            if (purchase.getTotalAmount() != null) {
                total = total.add(purchase.getTotalAmount());
            }
        }

        document.add(new Paragraph(" "));

        Font bold =
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

        document.add(new Paragraph(
                "Total Purchases : " + purchases.size(), bold));

        document.add(new Paragraph(
                "Subtotal : ₹ " + subtotal, bold));

        document.add(new Paragraph(
                "GST : ₹ " + gst, bold));

        document.add(new Paragraph(
                "Discount : ₹ " + discount, bold));

        document.add(new Paragraph(
                "Grand Total : ₹ " + total, bold));
    }

    @Override
    public ByteArrayInputStream exportProfitReport() {

        Document document = new Document(PageSize.A4);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfWriter.getInstance(document, out);

            document.open();

            Font title =
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD,
                            18);

            Paragraph p =
                    new Paragraph("PROFIT REPORT", title);

            p.setAlignment(Element.ALIGN_CENTER);

            document.add(p);

            document.add(new Paragraph(" "));

            BigDecimal sales =
                    saleRepository.getTotalSalesAmount();

            BigDecimal purchase =
                    purchaseRepository.getTotalPurchaseAmount();

            if (sales == null)
                sales = BigDecimal.ZERO;

            if (purchase == null)
                purchase = BigDecimal.ZERO;

            BigDecimal profit =
                    sales.subtract(purchase);

            document.add(new Paragraph(
                    "Total Sales : ₹ " + sales));

            document.add(new Paragraph(
                    "Total Purchase : ₹ " + purchase));

            document.add(new Paragraph(
                    "Net Profit : ₹ " + profit));

            document.close();

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Unable to generate Profit Report",
                    ex);
        }

        return new ByteArrayInputStream(
                out.toByteArray());
    }
}