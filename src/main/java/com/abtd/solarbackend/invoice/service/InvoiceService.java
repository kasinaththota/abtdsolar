package com.abtd.solarbackend.invoice.service;

import com.abtd.solarbackend.common.dto.PageRequestDto;
import com.abtd.solarbackend.common.dto.PageResponse;
import com.abtd.solarbackend.invoice.dto.request.CreateInvoiceRequest;
import com.abtd.solarbackend.invoice.dto.response.InvoiceResponse;

public interface InvoiceService {

    InvoiceResponse createInvoice(CreateInvoiceRequest request);

    InvoiceResponse getInvoiceById(Long id);

    InvoiceResponse getInvoiceBySale(Long saleId);

    PageResponse<InvoiceResponse> getAllInvoices(PageRequestDto pageRequest);
}