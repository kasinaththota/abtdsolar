package com.abtd.solarbackend.product.mapper;

import com.abtd.solarbackend.enums.ProductStatus;
import com.abtd.solarbackend.product.dto.request.CreateProductRequest;
import com.abtd.solarbackend.product.dto.request.UpdateProductRequest;
import com.abtd.solarbackend.product.dto.response.ProductResponse;
import com.abtd.solarbackend.product.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public Product toEntity(CreateProductRequest request) {
        return Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .productType(request.getProductType())
                .brand(request.getBrand())
                .manufacturer(request.getManufacturer())
                .model(request.getModel())
                .sku(request.getSku())
                .unit(request.getUnit())
                .purchasePrice(request.getPurchasePrice())
                .sellingPrice(request.getSellingPrice())
                .gstPercentage(request.getGstPercentage())
                .hsnCode(request.getHsnCode())
                .taxable(request.getTaxable())
                .inventoryItem(request.getInventoryItem())
                .minimumStock(request.getMinimumStock())
                .maximumStock(request.getMaximumStock())
                .warrantyYears(request.getWarrantyYears())
                .specification(request.getSpecification())
                .status(ProductStatus.ACTIVE)
                .build();
    }

    public ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .productCode(product.getProductCode())
                .sku(product.getSku())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .productType(product.getProductType())
                .brand(product.getBrand())
                .manufacturer(product.getManufacturer())
                .model(product.getModel())
                .unit(product.getUnit())
                .purchasePrice(product.getPurchasePrice())
                .sellingPrice(product.getSellingPrice())
                .gstPercentage(product.getGstPercentage())
                .hsnCode(product.getHsnCode())
                .taxable(product.getTaxable())
                .inventoryItem(product.getInventoryItem())
                .minimumStock(product.getMinimumStock())
                .maximumStock(product.getMaximumStock())
                .warrantyYears(product.getWarrantyYears())
                .specification(product.getSpecification())
                .status(product.getStatus())
                .createdAt(product.getCreatedAt())
                .build();
    }

    public void updateEntity(UpdateProductRequest request, Product product) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setProductType(request.getProductType());
        product.setBrand(request.getBrand());
        product.setManufacturer(request.getManufacturer());
        product.setModel(request.getModel());
        product.setSku(request.getSku());
        product.setUnit(request.getUnit());
        product.setPurchasePrice(request.getPurchasePrice());
        product.setSellingPrice(request.getSellingPrice());
        product.setGstPercentage(request.getGstPercentage());
        product.setHsnCode(request.getHsnCode());
        product.setTaxable(request.getTaxable());
        product.setInventoryItem(request.getInventoryItem());
        product.setMinimumStock(request.getMinimumStock());
        product.setMaximumStock(request.getMaximumStock());
        product.setWarrantyYears(request.getWarrantyYears());
        product.setSpecification(request.getSpecification());
    }
}