package com.abtd.solarbackend.purchase.exception;

public class PurchaseNotFoundException extends RuntimeException {

    public PurchaseNotFoundException(Long id) {
        super("Purchase not found with id : " + id);
    }

    public PurchaseNotFoundException(String message) {
        super(message);
    }
}