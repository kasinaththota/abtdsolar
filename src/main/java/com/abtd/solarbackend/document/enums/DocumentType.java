package com.abtd.solarbackend.document.enums;

public enum DocumentType {

    // Customer
    AADHAAR,
    PAN,
    PASSPORT,
    PHOTO,
    ADDRESS_PROOF,
    ELECTRICITY_BILL,

    // Vendor
    GST_CERTIFICATE,
    BANK_PROOF,
    AGREEMENT,

    // Product
    PRODUCT_IMAGE,
    DATASHEET,
    WARRANTY,

    // Purchase
    PURCHASE_INVOICE,
    DELIVERY_CHALLAN,

    // Sales
    SALES_INVOICE,
    INSTALLATION_PHOTO,

    OTHER
}