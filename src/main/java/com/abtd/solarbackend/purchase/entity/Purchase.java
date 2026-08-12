package com.abtd.solarbackend.purchase.entity;

import com.abtd.solarbackend.common.entity.BaseAuditableEntity;
import com.abtd.solarbackend.enums.PurchaseStatus;
import com.abtd.solarbackend.vendor.entity.Vendor;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchases")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Purchase extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String purchaseNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @Column(nullable = false)
    private LocalDate purchaseDate;

    private String invoiceNumber;

    private LocalDate invoiceDate;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal subtotal;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal gstAmount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal discountAmount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(length = 500)
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PurchaseStatus status;

    @OneToMany(
            mappedBy = "purchase",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<PurchaseItem> purchaseItems = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /**
     * Adds a purchase item and maintains both sides of the relationship.
     */
    public void addPurchaseItem(PurchaseItem item) {
        purchaseItems.add(item);
        item.setPurchase(this);
    }

    /**
     * Removes a purchase item and maintains both sides of the relationship.
     */
    public void removePurchaseItem(PurchaseItem item) {
        purchaseItems.remove(item);
        item.setPurchase(null);
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (status == null) {
            status = PurchaseStatus.DRAFT;
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
}