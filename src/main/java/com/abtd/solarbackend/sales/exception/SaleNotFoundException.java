package com.abtd.solarbackend.sales.exception;

public class SaleNotFoundException extends RuntimeException {

    public SaleNotFoundException(Long id) {
        super("Sale not found with id : " + id);
    }
}