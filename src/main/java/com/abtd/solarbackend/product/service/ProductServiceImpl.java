package com.abtd.solarbackend.product.service;

import com.abtd.solarbackend.common.constants.ProductMessages;
import com.abtd.solarbackend.common.dto.PageRequestDto;
import com.abtd.solarbackend.enums.ProductStatus;
import com.abtd.solarbackend.product.dto.request.CreateProductRequest;
import com.abtd.solarbackend.product.dto.request.UpdateProductRequest;
import com.abtd.solarbackend.product.dto.response.ProductResponse;
import com.abtd.solarbackend.product.entity.Product;
import com.abtd.solarbackend.product.exception.DuplicateProductException;
import com.abtd.solarbackend.product.exception.ProductNotFoundException;
import com.abtd.solarbackend.product.mapper.ProductMapper;
import com.abtd.solarbackend.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {

        validateDuplicateProduct(request);

        Product product = productMapper.toEntity(request);
        product.setProductCode(generateProductCode());

        Product savedProduct = productRepository.save(product);

        return productMapper.toResponse(savedProduct);
    }

    @Override
    public ProductResponse getProductById(Long id) {

        Product product = findProduct(id);

        return productMapper.toResponse(product);
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequestDto request) {

        Sort sort = request.getDirection().equalsIgnoreCase("DESC")
                ? Sort.by(request.getSortBy()).descending()
                : Sort.by(request.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                sort);

        return productRepository.findAll(pageable)
                .map(productMapper::toResponse);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(
            Long id,
            UpdateProductRequest request) {

        Product product = findProduct(id);

        if (!product.getSku().equalsIgnoreCase(request.getSku())
                && productRepository.existsBySku(request.getSku())) {

            throw new DuplicateProductException(
                    ProductMessages.PRODUCT_SKU_EXISTS);
        }

        productMapper.updateEntity(request, product);

        Product updatedProduct = productRepository.save(product);

        return productMapper.toResponse(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {

        Product product = findProduct(id);

        product.setStatus(ProductStatus.DISCONTINUED);

        productRepository.save(product);
    }

    @Override
    public Page<ProductResponse> getProductsByStatus(
            ProductStatus status,
            PageRequestDto request) {

        Sort sort = request.getDirection().equalsIgnoreCase("DESC")
                ? Sort.by(request.getSortBy()).descending()
                : Sort.by(request.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                sort);

        return productRepository.findAll(pageable)
                .map(productMapper::toResponse)
                .map(product -> product);
    }

    private Product findProduct(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                ProductMessages.PRODUCT_NOT_FOUND + id));
    }

    private void validateDuplicateProduct(CreateProductRequest request) {

        if (productRepository.existsBySku(request.getSku())) {
            throw new DuplicateProductException(
                    ProductMessages.PRODUCT_SKU_EXISTS);
        }

        if (productRepository.existsByNameIgnoreCaseAndBrandIgnoreCaseAndModelIgnoreCase(
                request.getName(),
                request.getBrand(),
                request.getModel())) {

            throw new DuplicateProductException(
                    ProductMessages.PRODUCT_ALREADY_EXISTS);
        }
    }

    private String generateProductCode() {

        return productRepository.findTopByOrderByIdDesc()
                .map(product -> {
                    long nextId = product.getId() + 1;
                    return String.format("PRD%06d", nextId);
                })
                .orElse("PRD000001");
    }
}

