package com.abtd.solarbackend.invoice.mapper;

import com.abtd.solarbackend.invoice.dto.response.InvoiceResponse;
import com.abtd.solarbackend.invoice.entity.Invoice;
import org.springframework.stereotype.Component;

@Component
public class InvoiceMapper {

    public InvoiceResponse toResponse(Invoice invoice) {

        InvoiceResponse response = new InvoiceResponse();

        response.setId(invoice.getId());
        response.setInvoiceNumber(invoice.getInvoiceNumber());

        response.setSaleId(invoice.getSale().getId());
        response.setSaleNumber(invoice.getSale().getSaleNumber());

        response.setCustomerName(
                invoice.getSale().getCustomer().getFirstName()
                        + " "
                        + invoice.getSale().getCustomer().getLastName());

        response.setInvoiceDate(invoice.getInvoiceDate());
        response.setDueDate(invoice.getDueDate());

        response.setTotalAmount(invoice.getSale().getTotalAmount());
        response.setPaidAmount(invoice.getSale().getPaidAmount());
        response.setBalanceAmount(invoice.getSale().getBalanceAmount());

        response.setPaid(invoice.getSale().getBalanceAmount().signum() == 0);

        return response;
    }
}