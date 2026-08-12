package com.abtd.solarbackend.purchase.service;

import com.abtd.solarbackend.common.dto.PageRequestDto;
import com.abtd.solarbackend.common.dto.PageResponse;
import com.abtd.solarbackend.enums.PurchaseStatus;
import com.abtd.solarbackend.purchase.dto.request.CreatePurchaseRequest;
import com.abtd.solarbackend.purchase.dto.request.UpdatePurchaseRequest;
import com.abtd.solarbackend.purchase.dto.response.PurchaseResponse;

public interface PurchaseService {

    PurchaseResponse createPurchase(CreatePurchaseRequest request);

    PurchaseResponse updatePurchase(
            Long id,
            UpdatePurchaseRequest request);

    PurchaseResponse getPurchaseById(Long id);

    PageResponse<PurchaseResponse> getAllPurchases(
            PageRequestDto pageRequest);

    PageResponse<PurchaseResponse> getPurchasesByStatus(
            PurchaseStatus status,
            PageRequestDto pageRequest);

    PurchaseResponse receivePurchase(Long id);
}