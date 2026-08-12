package com.abtd.solarbackend.quotation.entity;

import com.abtd.solarbackend.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "quotation_items")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotationItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quotation_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quotation_id")
    private Quotation quotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;

    @Column(precision = 15, scale = 2)
    private BigDecimal price;

    @Column(precision = 5, scale = 2)
    private BigDecimal gstPercentage;

    @Column(precision = 15, scale = 2)
    private BigDecimal discountAmount;

    @Column(precision = 15, scale = 2)
    private BigDecimal lineTotal;
}