package com.abtd.solarbackend.product.entity;

import com.abtd.solarbackend.common.entity.BaseAuditableEntity;
import com.abtd.solarbackend.enums.ProductCategory;
import com.abtd.solarbackend.enums.ProductStatus;
import com.abtd.solarbackend.enums.ProductType;
import com.abtd.solarbackend.enums.Unit;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_code", nullable = false, unique = true, length = 20)
    private String productCode;

    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType productType;

    @Column(nullable = false, length = 100)
    private String brand;

    @Column(length = 100)
    private String manufacturer;

    @Column(length = 100)
    private String model;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Unit unit;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal purchasePrice;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal sellingPrice;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal gstPercentage;

    @Column(length = 20)
    private String hsnCode;

    @Column(nullable = false)
    private Boolean taxable;

    @Column(nullable = false)
    private Boolean inventoryItem;

    @Column(nullable = false)
    private Integer minimumStock;

    @Column(nullable = false)
    private Integer maximumStock;

    @Column(nullable = false)
    private Integer warrantyYears;

    @Column(length = 3000)
    private String specification;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (status == null) {
            status = ProductStatus.ACTIVE;
        }

        if (taxable == null) {
            taxable = true;
        }

        if (inventoryItem == null) {
            inventoryItem = true;
        }

        if (minimumStock == null) {
            minimumStock = 0;
        }

        if (maximumStock == null) {
            maximumStock = 0;
        }

        if (warrantyYears == null) {
            warrantyYears = 0;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}