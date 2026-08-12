package com.abtd.solarbackend.sales.mapper;

import com.abtd.solarbackend.customer.entity.Customer;
import com.abtd.solarbackend.product.entity.Product;
import com.abtd.solarbackend.sales.dto.request.CreateSaleRequest;
import com.abtd.solarbackend.sales.dto.request.UpdateSaleRequest;
import com.abtd.solarbackend.sales.dto.response.SaleItemResponse;
import com.abtd.solarbackend.sales.dto.response.SaleResponse;
import com.abtd.solarbackend.sales.entity.Sale;
import com.abtd.solarbackend.sales.entity.SaleItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class SaleMapper {

    public Sale toEntity(CreateSaleRequest request, Customer customer) {

        Sale sale = new Sale();

        sale.setCustomer(customer);
        sale.setSaleDate(request.getSaleDate());
        sale.setInvoiceNumber(request.getInvoiceNumber());
        sale.setInvoiceDate(request.getInvoiceDate());
        sale.setDiscountAmount(request.getDiscountAmount());
        sale.setRemarks(request.getRemarks());

        return sale;
    }

    public void updateEntity(
            Sale sale,
            UpdateSaleRequest request,
            Customer customer) {

        sale.setCustomer(customer);
        sale.setSaleDate(request.getSaleDate());
        sale.setInvoiceNumber(request.getInvoiceNumber());
        sale.setInvoiceDate(request.getInvoiceDate());
        sale.setDiscountAmount(request.getDiscountAmount());
        sale.setRemarks(request.getRemarks());
    }

    public SaleResponse toResponse(Sale sale) {

        SaleResponse response = new SaleResponse();

        response.setId(sale.getId());
        response.setSaleNumber(sale.getSaleNumber());

        response.setCustomerId(sale.getCustomer().getId());
        response.setCustomerName(
                sale.getCustomer().getFirstName() + " " +
                        sale.getCustomer().getLastName()
        );

        response.setSaleDate(sale.getSaleDate());
        response.setInvoiceNumber(sale.getInvoiceNumber());
        response.setInvoiceDate(sale.getInvoiceDate());

        response.setSubtotal(sale.getSubtotal());
        response.setGstAmount(sale.getGstAmount());
        response.setDiscountAmount(sale.getDiscountAmount());
        response.setTotalAmount(sale.getTotalAmount());

        response.setRemarks(sale.getRemarks());
        response.setStatus(sale.getStatus());

        response.setSaleItems(
                sale.getSaleItems()
                        .stream()
                        .map(this::toItemResponse)
                        .collect(Collectors.toList())
        );

        return response;
    }

    public SaleItemResponse toItemResponse(SaleItem item) {

        SaleItemResponse response = new SaleItemResponse();

        Product product = item.getProduct();

        response.setId(item.getId());
        response.setProductId(product.getId());
        response.setProductName(product.getName());

        response.setQuantity(item.getQuantity());
        response.setUnitPrice(item.getUnitPrice());
        response.setGstPercentage(item.getGstPercentage());
        response.setGstAmount(item.getGstAmount());
        response.setTotalPrice(item.getTotalPrice());

        return response;
    }
}