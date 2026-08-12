package com.abtd.solarbackend.quotation.entity;

import com.abtd.solarbackend.customer.entity.Customer;
import com.abtd.solarbackend.enums.QuotationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quotations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Quotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quotation_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String quotationNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private LocalDate quotationDate;

    @Column(nullable = false)
    private LocalDate validTill;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal subtotal;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal gstAmount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal discountAmount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(length = 1000)
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuotationStatus status;

    @OneToMany(
            mappedBy = "quotation",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<QuotationItem> quotationItems = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (status == null) {
            status = QuotationStatus.DRAFT;
        }

        if (subtotal == null) {
            subtotal = BigDecimal.ZERO;
        }

        if (gstAmount == null) {
            gstAmount = BigDecimal.ZERO;
        }

        if (discountAmount == null) {
            discountAmount = BigDecimal.ZERO;
        }

        if (totalAmount == null) {
            totalAmount = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addQuotationItem(QuotationItem item) {
        quotationItems.add(item);
        item.setQuotation(this);
    }

    public void removeQuotationItem(QuotationItem item) {
        quotationItems.remove(item);
        item.setQuotation(null);
    }
}