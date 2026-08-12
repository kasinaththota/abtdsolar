package com.abtd.solarbackend.vendor.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateVendorRequest {

    @NotBlank(message = "Company name is required")
    @Size(max = 150)
    private String companyName;

    @NotBlank(message = "Contact person is required")
    @Size(max = 100)
    private String contactPerson;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$",
            message = "Invalid mobile number")
    private String mobile;

    @Pattern(regexp = "^[6-9]\\d{9}$",
            message = "Invalid alternate mobile number")
    private String alternateMobile;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    @Size(max = 100)
    private String email;

    @Size(max = 150)
    private String website;

    @NotBlank(message = "GST number is required")
    @Pattern(
            regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$",
            message = "Invalid GST number"
    )
    private String gstNumber;

    @Size(max = 20)
    private String panNumber;

    @NotBlank(message = "Address is required")
    @Size(max = 500)
    private String address;

    @NotBlank(message = "City is required")
    @Size(max = 100)
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 100)
    private String state;

    @NotBlank(message = "Pincode is required")
    @Pattern(regexp = "^\\d{6}$",
            message = "Invalid pincode")
    private String pincode;

    @Size(max = 100)
    private String country;

    @Size(max = 100)
    private String paymentTerms;

    @PositiveOrZero(message = "Credit days cannot be negative")
    private Integer creditDays;

    @Size(max = 100)
    private String bankName;

    @Size(max = 30)
    private String accountNumber;

    @Size(max = 20)
    private String ifscCode;

    @Size(max = 100)
    private String upiId;

    @Size(max = 1000)
    private String remarks;
}