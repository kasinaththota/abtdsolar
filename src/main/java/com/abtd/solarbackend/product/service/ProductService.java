package com.abtd.solarbackend.product.service;

import com.abtd.solarbackend.common.dto.PageRequestDto;
import com.abtd.solarbackend.enums.ProductStatus;
import com.abtd.solarbackend.product.dto.request.CreateProductRequest;
import com.abtd.solarbackend.product.dto.request.UpdateProductRequest;
import com.abtd.solarbackend.product.dto.response.ProductResponse;
import org.springframework.data.domain.Page;

public interface ProductService {
    ProductResponse createProduct(CreateProductRequest request);

    ProductResponse getProductById(Long id);

    Page<ProductResponse> getAllProducts(PageRequestDto request);

    ProductResponse updateProduct(Long id, UpdateProductRequest request);

    void deleteProduct(Long id);

    Page<ProductResponse> getProductsByStatus(ProductStatus status, PageRequestDto request);
}