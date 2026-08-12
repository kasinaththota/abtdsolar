package com.abtd.solarbackend.sales.controller;

import com.abtd.solarbackend.common.constants.SaleMessages;
import com.abtd.solarbackend.common.dto.PageRequestDto;
import com.abtd.solarbackend.common.dto.PageResponse;
import com.abtd.solarbackend.common.response.ApiResponse;
import com.abtd.solarbackend.common.response.ResponseBuilder;
import com.abtd.solarbackend.enums.SalesStatus;
import com.abtd.solarbackend.sales.dto.request.CreateSaleRequest;
import com.abtd.solarbackend.sales.dto.request.UpdateSaleRequest;
import com.abtd.solarbackend.sales.dto.response.SaleResponse;
import com.abtd.solarbackend.sales.service.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','SALES')")
public class SaleController {

    private final SaleService saleService;

    @PostMapping
    public ResponseEntity<ApiResponse<SaleResponse>> createSale(
            @Valid @RequestBody CreateSaleRequest request) {

        return ResponseBuilder.created(
                SaleMessages.SALE_CREATED,
                saleService.createSale(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SaleResponse>> updateSale(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSaleRequest request) {

        return ResponseBuilder.ok(
                SaleMessages.SALE_UPDATED,
                saleService.updateSale(id, request));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<SaleResponse>> completeSale(
            @PathVariable Long id) {

        return ResponseBuilder.ok(
                SaleMessages.SALE_COMPLETED,
                saleService.completeSale(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SaleResponse>> getSaleById(
            @PathVariable Long id) {

        return ResponseBuilder.ok(
                SaleMessages.SALE_FETCHED,
                saleService.getSaleById(id));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<SaleResponse>>> getAllSales(
            @ModelAttribute PageRequestDto pageRequest) {

        return ResponseBuilder.ok(
                SaleMessages.SALES_FETCHED,
                saleService.getAllSales(pageRequest));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<PageResponse<SaleResponse>>> getSalesByStatus(
            @PathVariable SalesStatus status,
            @ModelAttribute PageRequestDto pageRequest) {

        return ResponseBuilder.ok(
                SaleMessages.SALES_FETCHED,
                saleService.getSalesByStatus(status, pageRequest));
    }

}