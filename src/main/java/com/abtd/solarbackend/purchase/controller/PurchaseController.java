package com.abtd.solarbackend.purchase.controller;

import com.abtd.solarbackend.common.constants.PurchaseMessages;
import com.abtd.solarbackend.common.dto.PageRequestDto;
import com.abtd.solarbackend.common.dto.PageResponse;
import com.abtd.solarbackend.common.response.ApiResponse;
import com.abtd.solarbackend.common.response.ResponseBuilder;
import com.abtd.solarbackend.enums.PurchaseStatus;
import com.abtd.solarbackend.purchase.dto.request.CreatePurchaseRequest;
import com.abtd.solarbackend.purchase.dto.request.UpdatePurchaseRequest;
import com.abtd.solarbackend.purchase.dto.response.PurchaseResponse;
import com.abtd.solarbackend.purchase.service.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','PURCHASE')")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity<ApiResponse<PurchaseResponse>> createPurchase(
            @Valid @RequestBody CreatePurchaseRequest request) {

        return ResponseBuilder.created(
                PurchaseMessages.PURCHASE_CREATED,
                purchaseService.createPurchase(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PurchaseResponse>> updatePurchase(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePurchaseRequest request) {

        return ResponseBuilder.ok(
                PurchaseMessages.PURCHASE_UPDATED,
                purchaseService.updatePurchase(id, request));
    }

    @PutMapping("/{id}/receive")
    public ResponseEntity<ApiResponse<PurchaseResponse>> receivePurchase(
            @PathVariable Long id) {

        return ResponseBuilder.ok(
                PurchaseMessages.PURCHASE_RECEIVED,
                purchaseService.receivePurchase(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PurchaseResponse>> getPurchaseById(
            @PathVariable Long id) {

        return ResponseBuilder.ok(
                PurchaseMessages.PURCHASE_FETCHED,
                purchaseService.getPurchaseById(id));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PurchaseResponse>>> getAllPurchases(
            @ModelAttribute PageRequestDto pageRequest) {

        return ResponseBuilder.ok(
                PurchaseMessages.PURCHASES_FETCHED,
                purchaseService.getAllPurchases(pageRequest));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<PageResponse<PurchaseResponse>>> getPurchasesByStatus(
            @PathVariable PurchaseStatus status,
            @ModelAttribute PageRequestDto pageRequest) {

        return ResponseBuilder.ok(
                PurchaseMessages.PURCHASES_FETCHED,
                purchaseService.getPurchasesByStatus(
                        status,
                        pageRequest));
    }
}