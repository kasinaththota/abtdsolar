package com.abtd.solarbackend.inventory.entity;

import com.abtd.solarbackend.common.entity.BaseAuditableEntity;
import com.abtd.solarbackend.enums.InventoryStatus;
import com.abtd.solarbackend.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "inventory",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_inventory_product",
                        columnNames = "product_id"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Long id;

    /**
     * One inventory record per product.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "product_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_inventory_product")
    )
    private Product product;

    @Column(nullable = false)
    private Integer availableQuantity;

    @Column(nullable = false)
    private Integer reservedQuantity;

    @Column(nullable = false)
    private Integer minimumStock;

    @Column(nullable = false)
    private Integer maximumStock;

    @Column(length = 100)
    private String warehouseLocation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InventoryStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (availableQuantity == null) {
            availableQuantity = 0;
        }

        if (reservedQuantity == null) {
            reservedQuantity = 0;
        }

        if (minimumStock == null) {
            minimumStock = 0;
        }

        if (maximumStock == null) {
            maximumStock = 0;
        }

        updateInventoryStatus();
    }

    @PreUpdate
    public void preUpdate() {

        updatedAt = LocalDateTime.now();

        updateInventoryStatus();
    }

    private void updateInventoryStatus() {

        if (availableQuantity <= 0) {
            status = InventoryStatus.OUT_OF_STOCK;
        } else if (availableQuantity <= minimumStock) {
            status = InventoryStatus.LOW_STOCK;
        } else {
            status = InventoryStatus.AVAILABLE;
        }
    }

    /**
     * Quantity available for new orders after reservation.
     */
    @Transient
    public Integer getFreeQuantity() {

        return availableQuantity - reservedQuantity;
    }
}