package com.abtd.solarbackend.sales.service;

import com.abtd.solarbackend.common.dto.PageRequestDto;
import com.abtd.solarbackend.common.dto.PageResponse;
import com.abtd.solarbackend.enums.SalesStatus;
import com.abtd.solarbackend.sales.dto.request.CreateSaleRequest;
import com.abtd.solarbackend.sales.dto.request.UpdateSaleRequest;
import com.abtd.solarbackend.sales.dto.response.SaleResponse;

public interface SaleService {

    SaleResponse createSale(CreateSaleRequest request);

    SaleResponse updateSale(
            Long id,
            UpdateSaleRequest request);

    SaleResponse completeSale(Long id);

    SaleResponse getSaleById(Long id);

    PageResponse<SaleResponse> getAllSales(
            PageRequestDto pageRequest);

    PageResponse<SaleResponse> getSalesByStatus(
            SalesStatus status,
            PageRequestDto pageRequest);
}