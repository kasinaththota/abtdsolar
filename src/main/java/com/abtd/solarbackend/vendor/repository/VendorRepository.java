package com.abtd.solarbackend.vendor.repository;

import com.abtd.solarbackend.enums.VendorStatus;
import com.abtd.solarbackend.vendor.entity.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

    Optional<Vendor> findByVendorCode(String vendorCode);

    Optional<Vendor> findByEmail(String email);

    Optional<Vendor> findByMobile(String mobile);

    Optional<Vendor> findByGstNumber(String gstNumber);

    Page<Vendor> findByStatus(VendorStatus status, Pageable pageable);

    boolean existsByEmail(String email);

    boolean existsByMobile(String mobile);

    boolean existsByGstNumber(String gstNumber);

    Optional<Vendor> findTopByOrderByIdDesc();
}