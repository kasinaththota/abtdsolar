package com.abtd.solarbackend.customer.mapper;

import com.abtd.solarbackend.customer.dto.request.CreateCustomerRequest;
import com.abtd.solarbackend.customer.dto.request.UpdateCustomerRequest;
import com.abtd.solarbackend.customer.dto.response.CustomerResponse;
import com.abtd.solarbackend.customer.entity.Customer;
import com.abtd.solarbackend.enums.CustomerStatus;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toEntity(CreateCustomerRequest request) {

        return Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .mobile(request.getMobile())
                .alternateMobile(request.getAlternateMobile())
                .email(request.getEmail())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .electricityConsumerNumber(request.getElectricityConsumerNumber())
                .monthlyBill(request.getMonthlyBill())
                .sanctionedLoad(request.getSanctionedLoad())
                .roofType(request.getRoofType())
                .buildingType(request.getBuildingType())
                .leadSource(request.getLeadSource())
                .status(CustomerStatus.NEW)
                .build();
    }

    public CustomerResponse toResponse(Customer customer) {

        return CustomerResponse.builder()
                .id(customer.getId())
                .customerCode(customer.getCustomerCode())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .mobile(customer.getMobile())
                .alternateMobile(customer.getAlternateMobile())
                .email(customer.getEmail())
                .address(customer.getAddress())
                .city(customer.getCity())
                .state(customer.getState())
                .pincode(customer.getPincode())
                .electricityConsumerNumber(customer.getElectricityConsumerNumber())
                .monthlyBill(customer.getMonthlyBill())
                .sanctionedLoad(customer.getSanctionedLoad())
                .roofType(customer.getRoofType())
                .buildingType(customer.getBuildingType())
                .leadSource(customer.getLeadSource())
                .status(customer.getStatus())
                .createdAt(customer.getCreatedAt())
                .build();
    }

    public void updateEntity(UpdateCustomerRequest request, Customer customer) {

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setMobile(request.getMobile());
        customer.setAlternateMobile(request.getAlternateMobile());
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());
        customer.setCity(request.getCity());
        customer.setState(request.getState());
        customer.setPincode(request.getPincode());
        customer.setElectricityConsumerNumber(request.getElectricityConsumerNumber());
        customer.setMonthlyBill(request.getMonthlyBill());
        customer.setSanctionedLoad(request.getSanctionedLoad());
        customer.setRoofType(request.getRoofType());
        customer.setBuildingType(request.getBuildingType());
        customer.setLeadSource(request.getLeadSource());
    }
}