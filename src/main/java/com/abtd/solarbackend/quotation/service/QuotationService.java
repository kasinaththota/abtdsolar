package com.abtd.solarbackend.quotation.service;

import com.abtd.solarbackend.common.dto.PageRequestDto;
import com.abtd.solarbackend.quotation.dto.request.CreateQuotationRequest;
import com.abtd.solarbackend.quotation.dto.request.QuotationSearchRequest;
import com.abtd.solarbackend.quotation.dto.request.UpdateQuotationRequest;
import com.abtd.solarbackend.quotation.dto.response.QuotationDashboardResponse;
import com.abtd.solarbackend.quotation.dto.response.QuotationResponse;
import org.springframework.data.domain.Page;

public interface QuotationService {

    QuotationResponse createQuotation(CreateQuotationRequest request);

    QuotationResponse updateQuotation(
            Long id,
            UpdateQuotationRequest request);

    QuotationResponse getQuotationById(Long id);

    Page<QuotationResponse> getAllQuotations(
            PageRequestDto pageRequest);

    void deleteQuotation(Long id);

    QuotationResponse approveQuotation(Long id);

    Long convertToSale(Long quotationId);

    QuotationDashboardResponse getDashboard();

    Page<QuotationResponse> searchQuotations(
            QuotationSearchRequest request);
}