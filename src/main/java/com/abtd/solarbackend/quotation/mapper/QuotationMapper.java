package com.abtd.solarbackend.quotation.mapper;

import com.abtd.solarbackend.customer.entity.Customer;
import com.abtd.solarbackend.quotation.dto.response.QuotationItemResponse;
import com.abtd.solarbackend.quotation.dto.response.QuotationResponse;
import com.abtd.solarbackend.quotation.entity.Quotation;
import com.abtd.solarbackend.quotation.entity.QuotationItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuotationMapper {

    public QuotationResponse toResponse(Quotation quotation) {

        Customer customer = quotation.getCustomer();

        List<QuotationItemResponse> items =
                quotation.getQuotationItems()
                        .stream()
                        .map(this::toItemResponse)
                        .toList();

        return QuotationResponse.builder()
                .id(quotation.getId())
                .quotationNumber(quotation.getQuotationNumber())
                .customerId(customer.getId())
                .customerName(
                        customer.getFirstName() + " " +
                                customer.getLastName())
                .quotationDate(quotation.getQuotationDate())
                .validTill(quotation.getValidTill())
                .subtotal(quotation.getSubtotal())
                .gstAmount(quotation.getGstAmount())
                .discountAmount(quotation.getDiscountAmount())
                .totalAmount(quotation.getTotalAmount())
                .remarks(quotation.getRemarks())
                .status(quotation.getStatus())
                .items(items)
                .build();
    }

    private QuotationItemResponse toItemResponse(
            QuotationItem item) {

        return QuotationItemResponse.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productCode(item.getProduct().getProductCode())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .gstPercentage(item.getGstPercentage())
                .discountAmount(item.getDiscountAmount())
                .lineTotal(item.getLineTotal())
                .build();
    }
}