package com.abtd.solarbackend.product.dto.response;

import com.abtd.solarbackend.enums.ProductCategory;
import com.abtd.solarbackend.enums.ProductStatus;
import com.abtd.solarbackend.enums.ProductType;
import com.abtd.solarbackend.enums.Unit;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class ProductResponse {
    private Long id;
    private String productCode;
    private String sku;
    private String name;
    private String description;
    private ProductCategory category;
    private ProductType productType;
    private String brand;
    private String manufacturer;
    private String model;
    private Unit unit;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private BigDecimal gstPercentage;
    private String hsnCode;
    private Boolean taxable;
    private Boolean inventoryItem;
    private Integer minimumStock;
    private Integer maximumStock;
    private Integer warrantyYears;
    private String specification;
    private ProductStatus status;
    private LocalDateTime createdAt;
}