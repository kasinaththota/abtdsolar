package com.abtd.solarbackend.invoice.service;

public interface InvoicePdfService {

    byte[] generateInvoice(Long invoiceId);

}