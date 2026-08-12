package com.abtd.solarbackend.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private long totalCustomers;

    private long totalVendors;

    private long totalProducts;

    private long totalPurchases;

    private long totalSales;

    private BigDecimal purchaseAmount;

    private BigDecimal salesAmount;

    private BigDecimal revenue;

    private BigDecimal pendingPayments;

    private long totalInvoices;

    private long paidInvoices;

    private long pendingInvoices;

    private long lowStockProducts;

    private long outOfStockProducts;
}