package com.abtd.solarbackend.quotation.specification;

import com.abtd.solarbackend.quotation.dto.request.QuotationSearchRequest;
import com.abtd.solarbackend.quotation.entity.Quotation;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class QuotationSpecification {

    private QuotationSpecification() {
    }

    public static Specification<Quotation> search(
            QuotationSearchRequest request) {

        return (root, query, cb) -> {

            List<Predicate> predicates =
                    new ArrayList<>();

            if (request.getQuotationNumber() != null
                    && !request.getQuotationNumber().isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("quotationNumber")),
                                "%" + request.getQuotationNumber().toLowerCase() + "%"));
            }

            if (request.getCustomerId() != null) {

                predicates.add(
                        cb.equal(
                                root.get("customer").get("id"),
                                request.getCustomerId()));
            }

            if (request.getStatus() != null) {

                predicates.add(
                        cb.equal(
                                root.get("status"),
                                request.getStatus()));
            }

            if (request.getFromDate() != null
                    && request.getToDate() != null) {

                predicates.add(
                        cb.between(
                                root.get("quotationDate"),
                                request.getFromDate(),
                                request.getToDate()));
            }

            if (request.getMinAmount() != null) {

                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("totalAmount"),
                                request.getMinAmount()));
            }

            if (request.getMaxAmount() != null) {

                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("totalAmount"),
                                request.getMaxAmount()));
            }

            return cb.and(
                    predicates.toArray(
                            new Predicate[0]));
        };
    }
}