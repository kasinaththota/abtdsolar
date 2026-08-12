package com.abtd.solarbackend.sales.exception;

public class DuplicateSaleException extends RuntimeException {

    public DuplicateSaleException(String saleNumber) {
        super("Sale already exists with number : " + saleNumber);
    }
}