package com.abtd.solarbackend.invoice.service.impl;

import com.abtd.solarbackend.common.dto.PageRequestDto;
import com.abtd.solarbackend.common.dto.PageResponse;
import com.abtd.solarbackend.common.response.PageResponseBuilder;
import com.abtd.solarbackend.invoice.dto.request.CreateInvoiceRequest;
import com.abtd.solarbackend.invoice.dto.response.InvoiceResponse;
import com.abtd.solarbackend.invoice.entity.Invoice;
import com.abtd.solarbackend.invoice.exception.DuplicateInvoiceException;
import com.abtd.solarbackend.invoice.exception.InvoiceNotFoundException;
import com.abtd.solarbackend.invoice.mapper.InvoiceMapper;
import com.abtd.solarbackend.invoice.repository.InvoiceRepository;
import com.abtd.solarbackend.invoice.service.InvoiceService;
import com.abtd.solarbackend.sales.entity.Sale;
import com.abtd.solarbackend.sales.exception.SaleNotFoundException;
import com.abtd.solarbackend.sales.repository.SaleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;

    private final SaleRepository saleRepository;

    private final InvoiceMapper invoiceMapper;

    private Sale findSale(Long id) {

        return saleRepository.findById(id)
                .orElseThrow(() ->
                        new SaleNotFoundException(id));
    }

    private Invoice findInvoice(Long id) {

        return invoiceRepository.findById(id)
                .orElseThrow(() ->
                        new InvoiceNotFoundException(id));
    }

    private String generateInvoiceNumber() {

        return invoiceRepository.findTopByOrderByIdDesc()
                .map(invoice -> {

                    int number = Integer.parseInt(
                            invoice.getInvoiceNumber()
                                    .substring(3));

                    return String.format(
                            "INV%06d",
                            number + 1);

                })
                .orElse("INV000001");
    }

    @Override
    public InvoiceResponse createInvoice(CreateInvoiceRequest request) {

        if (invoiceRepository.existsBySaleId(request.getSaleId())) {

            throw new DuplicateInvoiceException(
                    request.getSaleId());
        }

        Sale sale = findSale(request.getSaleId());

        Invoice invoice = Invoice.builder()
                .invoiceNumber(generateInvoiceNumber())
                .sale(sale)
                .invoiceDate(sale.getInvoiceDate() != null
                        ? sale.getInvoiceDate()
                        : sale.getSaleDate())
                .dueDate(request.getDueDate())
                .generated(true)
                .paid(sale.getBalanceAmount().signum() == 0)
                .build();

        Invoice savedInvoice =
                invoiceRepository.save(invoice);

        return invoiceMapper.toResponse(savedInvoice);
    }

    @Override
    @Transactional
    public InvoiceResponse getInvoiceById(Long id) {

        return invoiceMapper.toResponse(
                findInvoice(id));
    }

    @Override
    @Transactional
    public InvoiceResponse getInvoiceBySale(Long saleId) {

        Invoice invoice =
                invoiceRepository.findBySaleId(saleId)
                        .orElseThrow(() ->
                                new InvoiceNotFoundException(saleId));

        return invoiceMapper.toResponse(invoice);
    }

    @Override
    @Transactional
    public PageResponse<InvoiceResponse> getAllInvoices(
            PageRequestDto pageRequest) {

        Pageable pageable =
                PageResponseBuilder.buildPageable(pageRequest);

        Page<Invoice> invoices =
                invoiceRepository.findAll(pageable);

        return PageResponseBuilder.build(
                invoices,
                invoiceMapper::toResponse);
    }
}