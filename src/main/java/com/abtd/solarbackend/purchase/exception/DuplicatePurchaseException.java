package com.abtd.solarbackend.purchase.exception;

public class DuplicatePurchaseException extends RuntimeException {

    public DuplicatePurchaseException(String message) {
        super(message);
    }
}