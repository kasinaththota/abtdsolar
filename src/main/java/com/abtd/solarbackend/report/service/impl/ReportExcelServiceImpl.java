package com.abtd.solarbackend.report.service.impl;

import com.abtd.solarbackend.inventory.entity.Inventory;
import com.abtd.solarbackend.inventory.repository.InventoryRepository;
import com.abtd.solarbackend.purchase.entity.Purchase;
import com.abtd.solarbackend.purchase.repository.PurchaseRepository;
import com.abtd.solarbackend.report.service.ReportExcelService;
import com.abtd.solarbackend.sales.entity.Sale;
import com.abtd.solarbackend.sales.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
public class ReportExcelServiceImpl implements ReportExcelService {

    private final SaleRepository saleRepository;
    private final PurchaseRepository purchaseRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public ByteArrayInputStream exportSalesReport(
            LocalDate fromDate,
            LocalDate toDate) {

        List<Sale> sales =
                saleRepository.findSalesBetween(fromDate, toDate);

        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream()
        ) {

            Sheet sheet = workbook.createSheet("Sales Report");

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);

            int rowNum = 0;

            Row companyRow = sheet.createRow(rowNum++);
            companyRow.createCell(0).setCellValue("ARAVINDH B SOLAR");

            Row titleRow = sheet.createRow(rowNum++);
            titleRow.createCell(0).setCellValue("SALES REPORT");

            Row fromRow = sheet.createRow(rowNum++);
            fromRow.createCell(0).setCellValue("From Date");
            fromRow.createCell(1).setCellValue(fromDate.toString());

            Row toRow = sheet.createRow(rowNum++);
            toRow.createCell(0).setCellValue("To Date");
            toRow.createCell(1).setCellValue(toDate.toString());

            rowNum++;

            Row header = sheet.createRow(rowNum++);

            String[] columns = {
                    "Sale No",
                    "Sale Date",
                    "Customer",
                    "Total",
                    "Paid",
                    "Balance"
            };

            for (int i = 0; i < columns.length; i++) {

                Cell cell = header.createCell(i);

                cell.setCellValue(columns[i]);

                cell.setCellStyle(headerStyle);
            }

            for (Sale sale : sales) {

                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(
                        getValue(sale.getSaleNumber()));

                row.createCell(1).setCellValue(
                        sale.getSaleDate() == null
                                ? ""
                                : sale.getSaleDate().toString());

                row.createCell(2).setCellValue(
                        sale.getCustomer() == null
                                ? ""
                                : getValue(
                                sale.getCustomer().getFirstName() + " " + sale.getCustomer().getLastName()));

                row.createCell(3).setCellValue(
                        sale.getTotalAmount() == null
                                ? 0
                                : sale.getTotalAmount().doubleValue());

                row.createCell(4).setCellValue(
                        sale.getPaidAmount() == null
                                ? 0
                                : sale.getPaidAmount().doubleValue());

                row.createCell(5).setCellValue(
                        sale.getBalanceAmount() == null
                                ? 0
                                : sale.getBalanceAmount().doubleValue());
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Failed to generate Sales Report Excel",
                    ex);
        }
    }

    private String getValue(String value) {

        return value == null ? "" : value;
    }

    @Override
    public ByteArrayInputStream exportPurchaseReport(
            LocalDate fromDate,
            LocalDate toDate) {

        List<Purchase> purchases =
                purchaseRepository.findPurchasesBetween(fromDate, toDate);

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Purchase Report");

            createPurchaseReport(sheet, workbook, purchases, fromDate, toDate);

            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception ex) {
            throw new RuntimeException(
                    "Failed to generate Purchase Excel Report",
                    ex);
        }
    }

    private void createPurchaseReport(
            Sheet sheet,
            Workbook workbook,
            List<Purchase> purchases,
            LocalDate fromDate,
            LocalDate toDate) {

        CellStyle titleStyle = workbook.createCellStyle();

        Font titleFont = workbook.createFont();

        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 16);

        titleStyle.setFont(titleFont);

        CellStyle headerStyle = workbook.createCellStyle();

        Font headerFont = workbook.createFont();

        headerFont.setBold(true);

        headerStyle.setFont(headerFont);

        int rowNum = 0;

        Row companyRow = sheet.createRow(rowNum++);

        companyRow.createCell(0)
                .setCellValue("ARAVINDH B SOLAR");

        companyRow.getCell(0)
                .setCellStyle(titleStyle);

        Row reportRow = sheet.createRow(rowNum++);

        reportRow.createCell(0)
                .setCellValue("PURCHASE REPORT");

        reportRow.getCell(0)
                .setCellStyle(titleStyle);

        rowNum++;

        sheet.createRow(rowNum++)
                .createCell(0)
                .setCellValue("From Date : " + fromDate);

        sheet.createRow(rowNum++)
                .createCell(0)
                .setCellValue("To Date : " + toDate);

        sheet.createRow(rowNum++)
                .createCell(0)
                .setCellValue("Generated On : " +
                        LocalDateTime.now().format(
                                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));

        rowNum++;

        Row header = sheet.createRow(rowNum++);
        String[] columns = {

                "Purchase No",
                "Purchase Date",
                "Vendor",
                "Invoice No",
                "Subtotal",
                "GST",
                "Discount",
                "Total",
                "Status"
        };

        for (int i = 0; i < columns.length; i++) {

            Cell cell = header.createCell(i);

            cell.setCellValue(columns[i]);

            cell.setCellStyle(headerStyle);
        }
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal gst = BigDecimal.ZERO;
        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;

        for (Purchase purchase : purchases) {

            Row row = sheet.createRow(rowNum++);

            row.createCell(0)
                    .setCellValue(getValue(
                            purchase.getPurchaseNumber()));

            row.createCell(1)
                    .setCellValue(
                            purchase.getPurchaseDate() == null
                                    ? ""
                                    : purchase.getPurchaseDate().toString());

            row.createCell(2)
                    .setCellValue(
                            purchase.getVendor() == null
                                    ? ""
                                    : purchase.getVendor().getCompanyName());

            row.createCell(3)
                    .setCellValue(getValue(
                            purchase.getInvoiceNumber()));

            row.createCell(4)
                    .setCellValue(
                            purchase.getSubtotal().doubleValue());

            row.createCell(5)
                    .setCellValue(
                            purchase.getGstAmount().doubleValue());

            row.createCell(6)
                    .setCellValue(
                            purchase.getDiscountAmount().doubleValue());

            row.createCell(7)
                    .setCellValue(
                            purchase.getTotalAmount().doubleValue());

            row.createCell(8)
                    .setCellValue(
                            purchase.getStatus().name());

            subtotal = subtotal.add(purchase.getSubtotal());

            gst = gst.add(purchase.getGstAmount());

            discount = discount.add(purchase.getDiscountAmount());

            total = total.add(purchase.getTotalAmount());
        }
        rowNum++;

        Row summary = sheet.createRow(rowNum++);

        summary.createCell(0)
                .setCellValue("Total Purchases");

        summary.createCell(1)
                .setCellValue(purchases.size());

        summary = sheet.createRow(rowNum++);

        summary.createCell(0)
                .setCellValue("Subtotal");

        summary.createCell(1)
                .setCellValue(subtotal.doubleValue());

        summary = sheet.createRow(rowNum++);

        summary.createCell(0)
                .setCellValue("GST");

        summary.createCell(1)
                .setCellValue(gst.doubleValue());

        summary = sheet.createRow(rowNum++);

        summary.createCell(0)
                .setCellValue("Discount");

        summary.createCell(1)
                .setCellValue(discount.doubleValue());

        summary = sheet.createRow(rowNum);

        summary.createCell(0)
                .setCellValue("Grand Total");

        summary.createCell(1)
                .setCellValue(total.doubleValue());
        for (int i = 0; i < 9; i++) {

            sheet.autoSizeColumn(i);
        }
    }

    @Override
    public ByteArrayInputStream exportInventoryReport() {

        List<Inventory> inventories =
                inventoryRepository.findAllForReport();

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Inventory Report");

            createInventoryReport(sheet, workbook, inventories);

            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Failed to generate Inventory Excel Report",
                    ex);
        }
    }

    private void createInventoryReport(
            Sheet sheet,
            Workbook workbook,
            List<Inventory> inventories) {

        CellStyle titleStyle = workbook.createCellStyle();

        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 16);

        titleStyle.setFont(titleFont);

        CellStyle headerStyle = workbook.createCellStyle();

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);

        headerStyle.setFont(headerFont);

        int rowNum = 0;

        // Company Name
        Row companyRow = sheet.createRow(rowNum++);
        companyRow.createCell(0).setCellValue("ARAVINDH B SOLAR");
        companyRow.getCell(0).setCellStyle(titleStyle);

        // Report Title
        Row reportRow = sheet.createRow(rowNum++);
        reportRow.createCell(0).setCellValue("INVENTORY REPORT");
        reportRow.getCell(0).setCellStyle(titleStyle);

        rowNum++;

        // Generated Date
        sheet.createRow(rowNum++)
                .createCell(0)
                .setCellValue(
                        "Generated On : " +
                                LocalDateTime.now().format(
                                        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));

        rowNum++;

        // Header
        Row header = sheet.createRow(rowNum++);

        String[] columns = {
                "Product Code",
                "SKU",
                "Product Name",
                "Brand",
                "Available Qty",
                "Reserved Qty",
                "Free Qty",
                "Min Stock",
                "Max Stock",
                "Warehouse",
                "Status"
        };

        for (int i = 0; i < columns.length; i++) {

            Cell cell = header.createCell(i);

            cell.setCellValue(columns[i]);

            cell.setCellStyle(headerStyle);
        }

        int totalAvailable = 0;
        int totalReserved = 0;
        int totalFree = 0;

        // Data
        for (Inventory inventory : inventories) {

            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(
                    inventory.getProduct() == null
                            ? ""
                            : getValue(inventory.getProduct().getProductCode()));

            row.createCell(1).setCellValue(
                    inventory.getProduct() == null
                            ? ""
                            : getValue(inventory.getProduct().getSku()));

            row.createCell(2).setCellValue(
                    inventory.getProduct() == null
                            ? ""
                            : getValue(inventory.getProduct().getName()));

            row.createCell(3).setCellValue(
                    inventory.getProduct() == null
                            ? ""
                            : getValue(inventory.getProduct().getBrand()));

            row.createCell(4).setCellValue(
                    inventory.getAvailableQuantity());

            row.createCell(5).setCellValue(
                    inventory.getReservedQuantity());

            row.createCell(6).setCellValue(
                    inventory.getFreeQuantity());

            row.createCell(7).setCellValue(
                    inventory.getMinimumStock());

            row.createCell(8).setCellValue(
                    inventory.getMaximumStock());

            row.createCell(9).setCellValue(
                    getValue(inventory.getWarehouseLocation()));

            row.createCell(10).setCellValue(
                    inventory.getStatus().name());

            totalAvailable += inventory.getAvailableQuantity();
            totalReserved += inventory.getReservedQuantity();
            totalFree += inventory.getFreeQuantity();
        }

        rowNum++;

        Row summary = sheet.createRow(rowNum++);
        summary.createCell(0).setCellValue("Total Products");
        summary.createCell(1).setCellValue(inventories.size());

        summary = sheet.createRow(rowNum++);
        summary.createCell(0).setCellValue("Available Quantity");
        summary.createCell(1).setCellValue(totalAvailable);

        summary = sheet.createRow(rowNum++);
        summary.createCell(0).setCellValue("Reserved Quantity");
        summary.createCell(1).setCellValue(totalReserved);

        summary = sheet.createRow(rowNum++);
        summary.createCell(0).setCellValue("Free Quantity");
        summary.createCell(1).setCellValue(totalFree);

        // Auto size columns
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    @Override
    public ByteArrayInputStream exportProfitReport() {

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Profit Report");

            createProfitReport(sheet, workbook);

            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Failed to generate Profit Excel Report",
                    ex);
        }
    }

    private void createProfitReport(
            Sheet sheet,
            Workbook workbook) {

        CellStyle titleStyle = workbook.createCellStyle();

        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 16);

        titleStyle.setFont(titleFont);

        int rowNum = 0;

        Row company = sheet.createRow(rowNum++);
        company.createCell(0).setCellValue("ARAVINDH B SOLAR");
        company.getCell(0).setCellStyle(titleStyle);

        Row report = sheet.createRow(rowNum++);
        report.createCell(0).setCellValue("PROFIT REPORT");
        report.getCell(0).setCellStyle(titleStyle);

        rowNum++;

        BigDecimal totalSales =
                saleRepository.getTotalSalesAmount();

        BigDecimal totalPurchase =
                purchaseRepository.getTotalPurchaseAmount();

        if (totalSales == null)
            totalSales = BigDecimal.ZERO;

        if (totalPurchase == null)
            totalPurchase = BigDecimal.ZERO;

        BigDecimal profit =
                totalSales.subtract(totalPurchase);

        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue("Total Sales");
        row.createCell(1).setCellValue(totalSales.doubleValue());

        row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue("Total Purchase");
        row.createCell(1).setCellValue(totalPurchase.doubleValue());

        row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue("Net Profit");
        row.createCell(1).setCellValue(profit.doubleValue());

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }
}