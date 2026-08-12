package com.abtd.solarbackend.customer.service;

import com.abtd.solarbackend.common.dto.PageRequestDto;
import com.abtd.solarbackend.customer.dto.request.CreateCustomerRequest;
import com.abtd.solarbackend.customer.dto.request.UpdateCustomerRequest;
import com.abtd.solarbackend.customer.dto.response.CustomerResponse;
import com.abtd.solarbackend.customer.entity.Customer;
import com.abtd.solarbackend.customer.exception.CustomerNotFoundException;
import com.abtd.solarbackend.customer.exception.DuplicateCustomerException;
import com.abtd.solarbackend.customer.mapper.CustomerMapper;
import com.abtd.solarbackend.customer.repository.CustomerRepository;
import com.abtd.solarbackend.document.dto.response.DocumentResponse;
import com.abtd.solarbackend.document.enums.DocumentType;
import com.abtd.solarbackend.document.enums.ModuleType;
import com.abtd.solarbackend.document.service.DocumentService;
import com.abtd.solarbackend.enums.CustomerStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final DocumentService documentService;

    private String generateCustomerCode() {

        return customerRepository.findTopByOrderByIdDesc()
                .map(customer -> {
                    int nextNumber = Integer.parseInt(customer.getCustomerCode().substring(3)) + 1;
                    return String.format("CUS%06d", nextNumber);
                })
                .orElse("CUS000001");
    }

    @Override
    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request) {

        // Check duplicate mobile number
        if (customerRepository.existsByMobile(request.getMobile())) {
            throw new DuplicateCustomerException(
                    "Customer with mobile number " + request.getMobile() + " already exists.");
        }

        // Check duplicate email (only if email is provided)
        if (request.getEmail() != null
                && !request.getEmail().isBlank()
                && customerRepository.existsByEmail(request.getEmail())) {

            throw new DuplicateCustomerException(
                    "Customer with email " + request.getEmail() + " already exists.");
        }

        // Convert Request DTO to Entity
        Customer customer = customerMapper.toEntity(request);

        // Generate Customer Code
        customer.setCustomerCode(generateCustomerCode());

        // Save Customer
        Customer savedCustomer = customerRepository.save(customer);

        // Convert Entity to Response DTO
        return customerMapper.toResponse(savedCustomer);
    }

    @Override
    public CustomerResponse getCustomerById(Long id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found with id : " + id));

        return customerMapper.toResponse(customer);
    }

    @Override
    public Page<CustomerResponse> getAllCustomers(PageRequestDto request) {

        Sort sort = request.getDirection().equalsIgnoreCase("asc")
                ? Sort.by(request.getSortBy()).ascending()
                : Sort.by(request.getSortBy()).descending();

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                sort);

        return customerRepository.findAll(pageable)
                .map(customerMapper::toResponse);
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomer(Long id,
                                           UpdateCustomerRequest request) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found with id : " + id));
        // Check mobile uniqueness
        if (!customer.getMobile().equals(request.getMobile())
                && customerRepository.existsByMobile(request.getMobile())) {

            throw new DuplicateCustomerException(
                    "Mobile number already exists.");
        }

        // Check email uniqueness
        if (request.getEmail() != null
                && !request.getEmail().isBlank()
                && !request.getEmail().equals(customer.getEmail())
                && customerRepository.existsByEmail(request.getEmail())) {

            throw new DuplicateCustomerException(
                    "Email already exists.");
        }

        customerMapper.updateEntity(request, customer);

        Customer updatedCustomer = customerRepository.save(customer);

        return customerMapper.toResponse(updatedCustomer);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found with id : " + id));

        customer.setStatus(CustomerStatus.CANCELLED);

        customerRepository.save(customer);
    }

    @Override
    public List<CustomerResponse> getCustomersByStatus(CustomerStatus status) {

        return customerRepository.findByStatus(status)
                .stream()
                .map(customerMapper::toResponse)
                .toList();
    }

    @Override
    public DocumentResponse uploadCustomerDocument(
            Long customerId,
            DocumentType documentType,
            String description,
            MultipartFile file) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found with id : " + customerId));

        return documentService.uploadDocument(
                ModuleType.CUSTOMER,
                customer.getId(),
                documentType,
                description,
                file);
    }

    @Override
    public List<DocumentResponse> getCustomerDocuments(
            Long customerId) {

        customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found with id : " + customerId));

        return documentService.getDocuments(
                ModuleType.CUSTOMER,
                customerId);
    }

    @Override
    public void deleteCustomerDocument(
            Long customerId,
            Long documentId) {

        customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found with id : " + customerId));

        documentService.deleteDocument(documentId);
    }

}
