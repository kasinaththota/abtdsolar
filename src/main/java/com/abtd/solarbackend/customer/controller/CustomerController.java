package com.abtd.solarbackend.customer.controller;

import com.abtd.solarbackend.common.constants.ApiMessages;
import com.abtd.solarbackend.common.dto.PageRequestDto;
import com.abtd.solarbackend.common.dto.PageResponse;
import com.abtd.solarbackend.common.response.ApiResponse;
import com.abtd.solarbackend.common.response.PageResponseBuilder;
import com.abtd.solarbackend.common.response.ResponseBuilder;
import com.abtd.solarbackend.customer.dto.request.CreateCustomerRequest;
import com.abtd.solarbackend.customer.dto.request.UpdateCustomerRequest;
import com.abtd.solarbackend.customer.dto.response.CustomerResponse;
import com.abtd.solarbackend.customer.service.CustomerService;
import com.abtd.solarbackend.document.enums.DocumentType;
import com.abtd.solarbackend.enums.CustomerStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','SALES')")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(
            @Valid @RequestBody CreateCustomerRequest request) {

        CustomerResponse response = customerService.createCustomer(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseBuilder.success(
                        HttpStatus.CREATED.value(),
                        ApiMessages.CUSTOMER_CREATED,
                        response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerById(
            @PathVariable Long id) {

        CustomerResponse response = customerService.getCustomerById(id);

        return ResponseEntity.ok(ResponseBuilder.success(
                HttpStatus.OK.value(),
                ApiMessages.CUSTOMER_FETCHED,
                response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CustomerResponse>>> getAllCustomers(
            PageRequestDto request) {

        Page<CustomerResponse> page =
                customerService.getAllCustomers(request);

        PageResponse<CustomerResponse> response =
                PageResponseBuilder.build(page);

        return ResponseEntity.ok(
                ResponseBuilder.success(
                        HttpStatus.OK.value(),
                        ApiMessages.CUSTOMER_FETCHED,
                        response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCustomerRequest request) {

        CustomerResponse response = customerService.updateCustomer(id, request);

        return ResponseEntity.ok(ResponseBuilder.success(
                HttpStatus.OK.value(),
                ApiMessages.CUSTOMER_UPDATED,
                response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(
            @PathVariable Long id) {

        customerService.deleteCustomer(id);

        return ResponseEntity.ok(ResponseBuilder.success(
                HttpStatus.OK.value(),
                ApiMessages.CUSTOMER_DELETED,
                null));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getCustomersByStatus(
            @PathVariable CustomerStatus status) {

        List<CustomerResponse> customers = customerService.getCustomersByStatus(status);

        return ResponseEntity.ok(ResponseBuilder.success(
                HttpStatus.OK.value(),
                ApiMessages.CUSTOMER_FETCHED,
                customers));
    }

    @PostMapping("/{id}/documents")
    public ResponseEntity<?> uploadCustomerDocument(

            @PathVariable Long id,

            @RequestParam DocumentType documentType,

            @RequestParam(required = false) String description,

            @RequestParam("file") MultipartFile file) {

        return ResponseBuilder.created(
                "Customer document uploaded successfully",
                customerService.uploadCustomerDocument(
                        id,
                        documentType,
                        description,
                        file));
    }

    @GetMapping("/{id}/documents")
    public ResponseEntity<?> getCustomerDocuments(
            @PathVariable Long id) {

        return ResponseBuilder.ok(
                "Customer documents fetched successfully",

                customerService.getCustomerDocuments(id));
    }

    @DeleteMapping("/{customerId}/documents/{documentId}")
    public ResponseEntity<?> deleteCustomerDocument(

            @PathVariable Long customerId,

            @PathVariable Long documentId) {

        customerService.deleteCustomerDocument(
                customerId,
                documentId);

        return ResponseBuilder.ok(
                "Customer document deleted successfully",
                null);
    }
}
