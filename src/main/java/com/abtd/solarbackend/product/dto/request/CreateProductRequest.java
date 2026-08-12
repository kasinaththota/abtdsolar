package com.abtd.solarbackend.product.dto.request;

import com.abtd.solarbackend.enums.ProductCategory;
import com.abtd.solarbackend.enums.ProductType;
import com.abtd.solarbackend.enums.Unit;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateProductRequest {
    @NotBlank(message = "Product name is required")
    @Size(max = 150)
    private String name;
    @Size(max = 1000)
    private String description;
    @NotNull(message = "Product category is required")
    private ProductCategory category;
    @NotNull(message = "Product type is required")
    private ProductType productType;
    @NotBlank(message = "Brand is required")
    @Size(max = 100)
    private String brand;
    @Size(max = 100)
    private String manufacturer;
    @Size(max = 100)
    private String model;
    @NotBlank(message = "SKU is required")
    @Size(max = 50)
    private String sku;
    @NotNull(message = "Unit is required")
    private Unit unit;
    @NotNull(message = "Purchase price is required")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal purchasePrice;
    @NotNull(message = "Selling price is required")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal sellingPrice;
    @NotNull(message = "GST percentage is required")
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "100.0")
    private BigDecimal gstPercentage;
    @Size(max = 20)
    private String hsnCode;
    @NotNull(message = "Taxable flag is required")
    private Boolean taxable;
    @NotNull(message = "Inventory item flag is required")
    private Boolean inventoryItem;
    @Min(0)
    private Integer minimumStock;
    @Min(0)
    private Integer maximumStock;
    @Min(0)
    @Max(50)
    private Integer warrantyYears;
    @Size(max = 3000)
    private String specification;
}