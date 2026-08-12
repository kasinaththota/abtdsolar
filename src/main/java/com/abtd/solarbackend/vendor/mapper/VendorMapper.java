package com.abtd.solarbackend.vendor.mapper;

import com.abtd.solarbackend.enums.VendorStatus;
import com.abtd.solarbackend.vendor.dto.request.CreateVendorRequest;
import com.abtd.solarbackend.vendor.dto.request.UpdateVendorRequest;
import com.abtd.solarbackend.vendor.dto.response.VendorResponse;
import com.abtd.solarbackend.vendor.entity.Vendor;
import org.springframework.stereotype.Component;

@Component
public class VendorMapper {

    public Vendor toEntity(CreateVendorRequest request) {

        return Vendor.builder()
                .companyName(request.getCompanyName())
                .contactPerson(request.getContactPerson())
                .mobile(request.getMobile())
                .alternateMobile(request.getAlternateMobile())
                .email(request.getEmail())
                .website(request.getWebsite())
                .gstNumber(request.getGstNumber())
                .panNumber(request.getPanNumber())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .country(request.getCountry())
                .paymentTerms(request.getPaymentTerms())
                .creditDays(request.getCreditDays())
                .bankName(request.getBankName())
                .accountNumber(request.getAccountNumber())
                .ifscCode(request.getIfscCode())
                .upiId(request.getUpiId())
                .remarks(request.getRemarks())
                .status(VendorStatus.ACTIVE)
                .build();
    }

    public VendorResponse toResponse(Vendor vendor) {

        return VendorResponse.builder()
                .id(vendor.getId())
                .vendorCode(vendor.getVendorCode())
                .companyName(vendor.getCompanyName())
                .contactPerson(vendor.getContactPerson())
                .mobile(vendor.getMobile())
                .alternateMobile(vendor.getAlternateMobile())
                .email(vendor.getEmail())
                .website(vendor.getWebsite())
                .gstNumber(vendor.getGstNumber())
                .panNumber(vendor.getPanNumber())
                .address(vendor.getAddress())
                .city(vendor.getCity())
                .state(vendor.getState())
                .pincode(vendor.getPincode())
                .country(vendor.getCountry())
                .paymentTerms(vendor.getPaymentTerms())
                .creditDays(vendor.getCreditDays())
                .bankName(vendor.getBankName())
                .accountNumber(vendor.getAccountNumber())
                .ifscCode(vendor.getIfscCode())
                .upiId(vendor.getUpiId())
                .remarks(vendor.getRemarks())
                .status(vendor.getStatus())
                .createdAt(vendor.getCreatedAt())
                .updatedAt(vendor.getUpdatedAt())
                .build();
    }

    public void updateEntity(UpdateVendorRequest request, Vendor vendor) {

        vendor.setCompanyName(request.getCompanyName());
        vendor.setContactPerson(request.getContactPerson());
        vendor.setMobile(request.getMobile());
        vendor.setAlternateMobile(request.getAlternateMobile());
        vendor.setEmail(request.getEmail());
        vendor.setWebsite(request.getWebsite());
        vendor.setGstNumber(request.getGstNumber());
        vendor.setPanNumber(request.getPanNumber());
        vendor.setAddress(request.getAddress());
        vendor.setCity(request.getCity());
        vendor.setState(request.getState());
        vendor.setPincode(request.getPincode());
        vendor.setCountry(request.getCountry());
        vendor.setPaymentTerms(request.getPaymentTerms());
        vendor.setCreditDays(request.getCreditDays());
        vendor.setBankName(request.getBankName());
        vendor.setAccountNumber(request.getAccountNumber());
        vendor.setIfscCode(request.getIfscCode());
        vendor.setUpiId(request.getUpiId());
        vendor.setRemarks(request.getRemarks());
    }
}