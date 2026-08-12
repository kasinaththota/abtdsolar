package com.abtd.solarbackend.vendor.dto.response;

import com.abtd.solarbackend.enums.VendorStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorResponse {

    private Long id;

    private String vendorCode;

    private String companyName;

    private String contactPerson;

    private String mobile;

    private String alternateMobile;

    private String email;

    private String website;

    private String gstNumber;

    private String panNumber;

    private String address;

    private String city;

    private String state;

    private String pincode;

    private String country;

    private String paymentTerms;

    private Integer creditDays;

    private String bankName;

    private String accountNumber;

    private String ifscCode;

    private String upiId;

    private String remarks;

    private VendorStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}