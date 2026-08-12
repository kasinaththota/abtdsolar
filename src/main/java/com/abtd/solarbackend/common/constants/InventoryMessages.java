package com.abtd.solarbackend.common.constants;

public final class InventoryMessages {

    public static final String INVENTORY_CREATED =
            "Inventory created successfully";
    public static final String INVENTORY_UPDATED =
            "Inventory updated successfully";
    public static final String INVENTORY_DELETED =
            "Inventory deleted successfully";
    public static final String INVENTORY_FETCHED =
            "Inventory fetched successfully";
    public static final String INVENTORIES_FETCHED =
            "Inventories fetched successfully";
    public static final String INVENTORY_ALREADY_EXISTS =
            "Inventory already exists for this product.";
    public static final String PRODUCT_NOT_FOUND =
            "Product not found.";
    public static final String INVALID_RESERVED_QUANTITY =
            "Reserved quantity cannot be greater than available quantity.";
    public static final String INVALID_STOCK_LIMITS =
            "Minimum stock cannot be greater than maximum stock.";

    private InventoryMessages() {
    }
}