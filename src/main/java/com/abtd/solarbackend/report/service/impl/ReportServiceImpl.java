package com.abtd.solarbackend.report.service.impl;

import com.abtd.solarbackend.inventory.entity.Inventory;
import com.abtd.solarbackend.inventory.repository.InventoryRepository;
import com.abtd.solarbackend.purchase.repository.PurchaseRepository;
import com.abtd.solarbackend.report.dto.response.InventoryReportResponse;
import com.abtd.solarbackend.report.dto.response.ProfitReportResponse;
import com.abtd.solarbackend.report.dto.response.PurchaseReportResponse;
import com.abtd.solarbackend.report.dto.response.SalesReportResponse;
import com.abtd.solarbackend.report.service.ReportService;
import com.abtd.solarbackend.sales.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final SaleRepository saleRepository;
    private final PurchaseRepository purchaseRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public SalesReportResponse getSalesReport(
            LocalDate fromDate,
            LocalDate toDate) {

        BigDecimal salesAmount =
                defaultValue(saleRepository.getSalesAmountBetween(fromDate, toDate));

        BigDecimal gstAmount =
                defaultValue(saleRepository.getGstAmountBetween(fromDate, toDate));

        BigDecimal discountAmount =
                defaultValue(saleRepository.getDiscountAmountBetween(fromDate, toDate));

        return SalesReportResponse.builder()
                .fromDate(fromDate)
                .toDate(toDate)
                .totalSales(saleRepository.countSalesBetween(fromDate, toDate))
                .salesAmount(salesAmount)
                .gstAmount(gstAmount)
                .discountAmount(discountAmount)
                .netAmount(salesAmount.subtract(discountAmount))
                .build();
    }

    @Override
    public PurchaseReportResponse getPurchaseReport(
            LocalDate fromDate,
            LocalDate toDate) {

        return PurchaseReportResponse.builder()
                .fromDate(fromDate)
                .toDate(toDate)
                .totalPurchases(
                        purchaseRepository.countPurchasesBetween(fromDate, toDate))
                .purchaseAmount(
                        defaultValue(
                                purchaseRepository.getPurchaseAmountBetween(fromDate, toDate)))
                .gstAmount(
                        defaultValue(
                                purchaseRepository.getPurchaseGstBetween(fromDate, toDate)))
                .build();
    }

    @Override
    public List<InventoryReportResponse> getInventoryReport() {

        return inventoryRepository.findAll()
                .stream()
                .map(this::mapInventory)
                .toList();
    }

    @Override
    public ProfitReportResponse getProfitReport() {

        BigDecimal sales =
                defaultValue(saleRepository.getTotalSalesAmount());

        BigDecimal purchase =
                defaultValue(purchaseRepository.getTotalPurchaseAmount());

        return ProfitReportResponse.builder()
                .salesAmount(sales)
                .purchaseAmount(purchase)
                .profit(sales.subtract(purchase))
                .build();
    }

    private InventoryReportResponse mapInventory(Inventory inventory) {

        return InventoryReportResponse.builder()
                .productCode(inventory.getProduct().getProductCode())
                .productName(inventory.getProduct().getName())
                .availableQuantity(inventory.getAvailableQuantity())
                .reservedQuantity(inventory.getReservedQuantity())
                .minimumStock(inventory.getMinimumStock())
                .maximumStock(inventory.getMaximumStock())
                .status(inventory.getStatus().name())
                .build();
    }

    private BigDecimal defaultValue(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}