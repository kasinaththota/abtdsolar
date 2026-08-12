package com.abtd.solarbackend.report.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryReportResponse {

    private String productCode;

    private String productName;

    private Integer availableQuantity;

    private Integer reservedQuantity;

    private Integer minimumStock;

    private Integer maximumStock;

    private String status;

}