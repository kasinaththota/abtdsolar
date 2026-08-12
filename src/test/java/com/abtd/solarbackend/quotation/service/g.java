package com.abtd.solarbackend.quotation.service;

import com.abtd.solarbackend.customer.entity.Customer;
import com.abtd.solarbackend.customer.repository.CustomerRepository;
import com.abtd.solarbackend.product.repository.ProductRepository;
import com.abtd.solarbackend.quotation.mapper.QuotationMapper;
import com.abtd.solarbackend.quotation.repository.QuotationRepository;
import com.abtd.solarbackend.quotation.service.impl.QuotationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class QuotationServiceImplTest {

    @Mock
    private QuotationRepository quotationRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private QuotationMapper quotationMapper;

    @InjectMocks
    private QuotationServiceImpl quotationService;

    private Customer customer;

    @BeforeEach
    void setUp() {

        customer = new Customer();

        customer.setId(1L);
    }

    @Test
    void shouldFindCustomer() {

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        Customer dbCustomer =
                customerRepository.findById(1L).get();

        assertNotNull(dbCustomer);

        assertEquals(1L, dbCustomer.getId());

        verify(customerRepository).findById(1L);
    }

}