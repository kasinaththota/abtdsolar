package com.abtd.solarbackend.product.controller;

import com.abtd.solarbackend.common.constants.ProductMessages;
import com.abtd.solarbackend.common.dto.PageRequestDto;
import com.abtd.solarbackend.common.dto.PageResponse;
import com.abtd.solarbackend.common.response.ApiResponse;
import com.abtd.solarbackend.common.response.PageResponseBuilder;
import com.abtd.solarbackend.common.response.ResponseBuilder;
import com.abtd.solarbackend.enums.ProductStatus;
import com.abtd.solarbackend.product.dto.request.CreateProductRequest;
import com.abtd.solarbackend.product.dto.request.UpdateProductRequest;
import com.abtd.solarbackend.product.dto.response.ProductResponse;
import com.abtd.solarbackend.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','INVENTORY')")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody CreateProductRequest request) {

        ProductResponse response = productService.createProduct(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseBuilder.success(
                        HttpStatus.CREATED.value(),
                        ProductMessages.PRODUCT_CREATED,
                        response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            @PathVariable Long id) {

        ProductResponse response = productService.getProductById(id);

        return ResponseEntity.ok(
                ResponseBuilder.success(
                        HttpStatus.OK.value(),
                        ProductMessages.PRODUCT_FETCHED,
                        response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getAllProducts(
            PageRequestDto request) {

        Page<ProductResponse> page =
                productService.getAllProducts(request);

        PageResponse<ProductResponse> response =
                PageResponseBuilder.build(page);

        return ResponseEntity.ok(
                ResponseBuilder.success(
                        HttpStatus.OK.value(),
                        ProductMessages.PRODUCTS_FETCHED,
                        response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request) {

        ProductResponse response =
                productService.updateProduct(id, request);

        return ResponseEntity.ok(
                ResponseBuilder.success(
                        HttpStatus.OK.value(),
                        ProductMessages.PRODUCT_UPDATED,
                        response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable Long id) {

        productService.deleteProduct(id);

        return ResponseEntity.ok(
                ResponseBuilder.success(
                        HttpStatus.OK.value(),
                        ProductMessages.PRODUCT_DELETED,
                        null));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getProductsByStatus(
            @PathVariable ProductStatus status,
            PageRequestDto request) {

        Page<ProductResponse> page =
                productService.getProductsByStatus(status, request);

        PageResponse<ProductResponse> response =
                PageResponseBuilder.build(page);

        return ResponseEntity.ok(
                ResponseBuilder.success(
                        HttpStatus.OK.value(),
                        ProductMessages.PRODUCTS_FETCHED,
                        response));
    }
}

