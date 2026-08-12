package com.abtd.solarbackend.customer.entity;

import com.abtd.solarbackend.common.entity.BaseAuditableEntity;
import com.abtd.solarbackend.enums.BuildingType;
import com.abtd.solarbackend.enums.CustomerStatus;
import com.abtd.solarbackend.enums.LeadSource;
import com.abtd.solarbackend.enums.RoofType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @Column(name = "customer_code", unique = true, nullable = false, length = 20)
    private String customerCode;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(nullable = false, unique = true, length = 10)
    private String mobile;

    @Column(name = "alternate_mobile", length = 10)
    private String alternateMobile;

    @Column(length = 100)
    private String email;

    @Column(length = 500)
    private String address;

    @Column(length = 50)
    private String city;

    @Column(length = 50)
    private String state;

    @Column(length = 6)
    private String pincode;

    @Column(name = "electricity_consumer_number", length = 30)
    private String electricityConsumerNumber;

    @Column(name = "monthly_bill")
    private BigDecimal monthlyBill;

    @Column(name = "sanctioned_load")
    private Double sanctionedLoad;

    @Enumerated(EnumType.STRING)
    @Column(name = "roof_type")
    private RoofType roofType;

    @Enumerated(EnumType.STRING)
    @Column(name = "building_type")
    private BuildingType buildingType;

    @Enumerated(EnumType.STRING)
    @Column(name = "lead_source")
    private LeadSource leadSource;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerStatus status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (status == null) {
            status = CustomerStatus.NEW;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}