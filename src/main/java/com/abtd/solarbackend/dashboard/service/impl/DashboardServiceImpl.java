package com.abtd.solarbackend.dashboard.service.impl;

import com.abtd.solarbackend.customer.repository.CustomerRepository;
import com.abtd.solarbackend.dashboard.dto.response.DashboardResponse;
import com.abtd.solarbackend.dashboard.service.DashboardService;
import com.abtd.solarbackend.inventory.repository.InventoryRepository;
import com.abtd.solarbackend.invoice.repository.InvoiceRepository;
import com.abtd.solarbackend.payment.repository.PaymentRepository;
import com.abtd.solarbackend.product.repository.ProductRepository;
import com.abtd.solarbackend.purchase.repository.PurchaseRepository;
import com.abtd.solarbackend.sales.repository.SaleRepository;
import com.abtd.solarbackend.vendor.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final CustomerRepository customerRepository;

    private final VendorRepository vendorRepository;

    private final ProductRepository productRepository;

    private final PurchaseRepository purchaseRepository;

    private final SaleRepository saleRepository;

    private final PaymentRepository paymentRepository;

    private final InventoryRepository inventoryRepository;

    private final InvoiceRepository invoiceRepository;

    @Override
    public DashboardResponse getDashboard() {

        long totalCustomers = customerRepository.count();

        long totalVendors = vendorRepository.count();

        long totalProducts = productRepository.count();

        long totalPurchases = purchaseRepository.count();

        long totalSales = saleRepository.count();

        BigDecimal purchaseAmount =
                defaultValue(
                        purchaseRepository.getTotalPurchaseAmount());

        BigDecimal salesAmount =
                defaultValue(
                        saleRepository.getTotalSalesAmount());

        BigDecimal pendingPayments =
                defaultValue(
                        saleRepository.getTotalPendingPayments());

        long totalInvoices =
                invoiceRepository.count();

        long paidInvoices =
                invoiceRepository.countByPaidTrue();

        long pendingInvoices =
                invoiceRepository.countByPaidFalse();

        long lowStockProducts =
                inventoryRepository.getLowStockProducts();

        long outOfStockProducts =
                inventoryRepository.getOutOfStockProducts();

        BigDecimal revenue =
                salesAmount.subtract(purchaseAmount);

        return DashboardResponse.builder()
                .totalCustomers(totalCustomers)
                .totalVendors(totalVendors)
                .totalProducts(totalProducts)
                .totalPurchases(totalPurchases)
                .totalSales(totalSales)
                .purchaseAmount(purchaseAmount)
                .salesAmount(salesAmount)
                .revenue(revenue)
                .pendingPayments(pendingPayments)
                .totalInvoices(totalInvoices)
                .paidInvoices(paidInvoices)
                .pendingInvoices(pendingInvoices)
                .lowStockProducts(lowStockProducts)
                .outOfStockProducts(outOfStockProducts)
                .build();
    }

    private BigDecimal defaultValue(BigDecimal value) {

        return value == null
                ? BigDecimal.ZERO
                : value;
    }

}