package com.abtd.solarbackend.customer.dto.response;

import com.abtd.solarbackend.enums.BuildingType;
import com.abtd.solarbackend.enums.CustomerStatus;
import com.abtd.solarbackend.enums.LeadSource;
import com.abtd.solarbackend.enums.RoofType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class CustomerResponse {

    private Long id;

    private String customerCode;

    private String firstName;

    private String lastName;

    private String mobile;

    private String alternateMobile;

    private String email;

    private String address;

    private String city;

    private String state;

    private String pincode;

    private String electricityConsumerNumber;

    private BigDecimal monthlyBill;

    private Double sanctionedLoad;

    private RoofType roofType;

    private BuildingType buildingType;

    private LeadSource leadSource;

    private CustomerStatus status;

    private LocalDateTime createdAt;
}