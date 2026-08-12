package com.abtd.solarbackend.vendor.entity;

import com.abtd.solarbackend.common.entity.BaseAuditableEntity;
import com.abtd.solarbackend.enums.VendorStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "vendors")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vendor extends BaseAuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vendor_id")
    private Long id;

    @Column(name = "vendor_code", nullable = false, unique = true, length = 20)
    private String vendorCode;

    @Column(nullable = false, length = 150)
    private String companyName;

    @Column(nullable = false, length = 100)
    private String contactPerson;

    @Column(nullable = false, unique = true, length = 15)
    private String mobile;

    @Column(length = 15)
    private String alternateMobile;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 150)
    private String website;

    @Column(nullable = false, unique = true, length = 20)
    private String gstNumber;

    @Column(length = 20)
    private String panNumber;

    @Column(nullable = false, length = 500)
    private String address;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String state;

    @Column(nullable = false, length = 10)
    private String pincode;

    @Column(nullable = false, length = 100)
    private String country;

    @Column(length = 100)
    private String paymentTerms;

    private Integer creditDays;

    @Column(length = 100)
    private String bankName;

    @Column(length = 30)
    private String accountNumber;

    @Column(length = 20)
    private String ifscCode;

    @Column(length = 100)
    private String upiId;

    @Column(length = 1000)
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VendorStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (status == null) {
            status = VendorStatus.ACTIVE;
        }

        if (creditDays == null) {
            creditDays = 0;
        }

        if (country == null || country.isBlank()) {
            country = "India";
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
