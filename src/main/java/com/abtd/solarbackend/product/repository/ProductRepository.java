package com.abtd.solarbackend.product.repository;

import com.abtd.solarbackend.enums.ProductCategory;
import com.abtd.solarbackend.enums.ProductStatus;
import com.abtd.solarbackend.enums.ProductType;
import com.abtd.solarbackend.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByProductCode(String productCode);

    Optional<Product> findBySku(String sku);

    List<Product> findByStatus(ProductStatus status);

    List<Product> findByCategory(ProductCategory category);

    List<Product> findByBrandIgnoreCase(String brand);

    List<Product> findByProductType(ProductType productType);

    List<Product> findByCategoryAndStatus(
            ProductCategory category,
            ProductStatus status);

    boolean existsByProductCode(String productCode);

    boolean existsBySku(String sku);

    boolean existsByNameIgnoreCaseAndBrandIgnoreCaseAndModelIgnoreCase(
            String name,
            String brand,
            String model);

    Optional<Product> findTopByOrderByIdDesc();
}