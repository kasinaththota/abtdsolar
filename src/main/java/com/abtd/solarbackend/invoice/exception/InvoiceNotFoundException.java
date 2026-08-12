package com.abtd.solarbackend.invoice.exception;

public class InvoiceNotFoundException extends RuntimeException {

    public InvoiceNotFoundException(Long id) {
        super("Invoice not found with id : " + id);
    }
}