package com.abtd.solarbackend.customer.dto.request;

import com.abtd.solarbackend.enums.BuildingType;
import com.abtd.solarbackend.enums.LeadSource;
import com.abtd.solarbackend.enums.RoofType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateCustomerRequest {

    @NotBlank
    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Pattern(regexp = "^[6-9]\\d{9}$")
    private String mobile;

    @Pattern(regexp = "^[6-9]\\d{9}$")
    private String alternateMobile;

    @Email
    private String email;

    private String address;

    private String city;

    private String state;

    @Pattern(regexp = "^\\d{6}$")
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