package com.abtd.solarbackend.customer.dto.request;

import com.abtd.solarbackend.enums.BuildingType;
import com.abtd.solarbackend.enums.LeadSource;
import com.abtd.solarbackend.enums.RoofType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateCustomerRequest {

    @NotBlank(message = "First name is required")
    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$",
            message = "Invalid mobile number")
    private String mobile;

    @Pattern(regexp = "^[6-9]\\d{9}$",
            message = "Invalid alternate mobile number")
    private String alternateMobile;

    @Email(message = "Invalid email address")
    private String email;

    @Size(max = 500)
    private String address;

    private String city;

    private String state;

    @Pattern(regexp = "^\\d{6}$",
            message = "Pincode must be 6 digits")
    private String pincode;

    private String electricityConsumerNumber;

    @PositiveOrZero
    private BigDecimal monthlyBill;

    @PositiveOrZero
    private Double sanctionedLoad;

    private RoofType roofType;

    private BuildingType buildingType;

    private LeadSource leadSource;
}