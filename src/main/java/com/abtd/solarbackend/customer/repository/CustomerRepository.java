package com.abtd.solarbackend.customer.repository;

import com.abtd.solarbackend.customer.entity.Customer;
import com.abtd.solarbackend.enums.CustomerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByCustomerCode(String customerCode);
    Optional<Customer> findByMobile(String mobile);
    Optional<Customer> findByEmail(String email);
    List<Customer> findByStatus(String status);
    List<Customer> findByStatus(CustomerStatus status);
    boolean existsByMobile(String mobile);
    boolean existsByEmail(String email);
    Optional<Customer> findTopByOrderByIdDesc();
}
