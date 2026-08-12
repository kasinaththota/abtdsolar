package com.abtd.solarbackend.invoice.exception;

public class DuplicateInvoiceException extends RuntimeException {

    public DuplicateInvoiceException(Long saleId) {
        super("Invoice already exists for sale id : " + saleId);
    }
}