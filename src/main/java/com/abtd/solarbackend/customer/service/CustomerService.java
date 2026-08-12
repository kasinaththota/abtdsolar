package com.abtd.solarbackend.customer.service;

import com.abtd.solarbackend.common.dto.PageRequestDto;
import com.abtd.solarbackend.customer.dto.request.CreateCustomerRequest;
import com.abtd.solarbackend.customer.dto.request.UpdateCustomerRequest;
import com.abtd.solarbackend.customer.dto.response.CustomerResponse;
import com.abtd.solarbackend.document.dto.response.DocumentResponse;
import com.abtd.solarbackend.document.enums.DocumentType;
import com.abtd.solarbackend.enums.CustomerStatus;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomerService {

    CustomerResponse createCustomer(CreateCustomerRequest request);

    CustomerResponse getCustomerById(Long id);

    Page<CustomerResponse> getAllCustomers(PageRequestDto pageRequest);

    CustomerResponse updateCustomer(Long id, UpdateCustomerRequest request);

    void deleteCustomer(Long id);

    List<CustomerResponse> getCustomersByStatus(CustomerStatus status);

    DocumentResponse uploadCustomerDocument(
            Long customerId,
            DocumentType documentType,
            String description,
            MultipartFile file);

    List<DocumentResponse> getCustomerDocuments(
            Long customerId);

    void deleteCustomerDocument(
            Long customerId,
            Long documentId);
}
