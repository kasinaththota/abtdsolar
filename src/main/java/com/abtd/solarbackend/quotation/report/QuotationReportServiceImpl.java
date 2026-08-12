package com.abtd.solarbackend.quotation.report;

import com.abtd.solarbackend.quotation.entity.Quotation;
import com.abtd.solarbackend.quotation.repository.QuotationRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuotationReportServiceImpl
        implements QuotationReportService {

    private final QuotationRepository quotationRepository;

    @Override
    public ByteArrayInputStream exportQuotationExcel() {

        List<Quotation> quotations =
                quotationRepository.findAll();

        try {

            Workbook workbook =
                    new XSSFWorkbook();

            Sheet sheet =
                    workbook.createSheet("Quotations");
            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("Quotation No");

            header.createCell(1).setCellValue("Customer");

            header.createCell(2).setCellValue("Date");

            header.createCell(3).setCellValue("Status");

            header.createCell(4).setCellValue("Total Amount");

            int rowNum = 1;

            for (Quotation quotation : quotations) {

                Row row = sheet.createRow(rowNum++);

                row.createCell(0)
                        .setCellValue(
                                quotation.getQuotationNumber());

                row.createCell(1)
                        .setCellValue(
                                quotation.getCustomer()
                                        .getFirstName()
                                        + " "
                                        + quotation.getCustomer()
                                        .getLastName());

                row.createCell(2)
                        .setCellValue(
                                quotation.getQuotationDate()
                                        .toString());

                row.createCell(3)
                        .setCellValue(
                                quotation.getStatus()
                                        .name());

                row.createCell(4)
                        .setCellValue(
                                quotation.getTotalAmount()
                                        .doubleValue());
            }

            for (int i = 0; i < 5; i++) {

                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out =
                    new ByteArrayOutputStream();

            workbook.write(out);

            workbook.close();

            return new ByteArrayInputStream(
                    out.toByteArray());

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Unable to export quotations",
                    ex);
        }
    }
}