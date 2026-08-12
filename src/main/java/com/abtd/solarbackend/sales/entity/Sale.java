package com.abtd.solarbackend.sales.entity;

import com.abtd.solarbackend.common.entity.BaseAuditableEntity;
import com.abtd.solarbackend.customer.entity.Customer;
import com.abtd.solarbackend.enums.PaymentStatus;
import com.abtd.solarbackend.enums.SalesStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sale extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id")
    private Long id;

    @Column(name = "sale_number", nullable = false, unique = true)
    private String saleNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "sale_date", nullable = false)
    private LocalDate saleDate;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @Column(name = "invoice_date")
    private LocalDate invoiceDate;

    @Column(name = "subtotal", precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "gst_amount", precision = 12, scale = 2)
    private BigDecimal gstAmount;

    @Column(name = "discount_amount", precision = 12, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "total_amount", precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "paid_amount", precision = 12, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(name = "balance_amount", precision = 12, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal balanceAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(length = 500)
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SalesStatus status;

    @OneToMany(
            mappedBy = "sale",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<SaleItem> saleItems = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void addSaleItem(SaleItem item) {
        saleItems.add(item);
        item.setSale(this);
    }

    public void removeSaleItem(SaleItem item) {
        saleItems.remove(item);
        item.setSale(null);
    }

    @PrePersist
    public void prePersist() {

        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if (this.status == null) {
            this.status = SalesStatus.DRAFT;
        }

        if (this.subtotal == null) {
            this.subtotal = BigDecimal.ZERO;
        }

        if (this.gstAmount == null) {
            this.gstAmount = BigDecimal.ZERO;
        }

        if (this.discountAmount == null) {
            this.discountAmount = BigDecimal.ZERO;
        }

        if (this.totalAmount == null) {
            this.totalAmount = BigDecimal.ZERO;
        }

        if (this.paidAmount == null) {
            this.paidAmount = BigDecimal.ZERO;
        }

        if (this.balanceAmount == null) {
            this.balanceAmount = this.totalAmount;
        }

        if (this.paymentStatus == null) {
            this.paymentStatus = PaymentStatus.PENDING;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}