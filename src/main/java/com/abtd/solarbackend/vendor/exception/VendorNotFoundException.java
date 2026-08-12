package com.abtd.solarbackend.vendor.exception;

public class VendorNotFoundException extends RuntimeException {

    public VendorNotFoundException(Long id) {
        super("Vendor not found with id : " + id);
    }

    public VendorNotFoundException(String vendorCode) {
        super("Vendor not found with vendor code : " + vendorCode);
    }
}