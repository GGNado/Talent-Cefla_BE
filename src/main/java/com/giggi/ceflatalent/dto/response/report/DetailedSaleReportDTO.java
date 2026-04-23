package com.giggi.ceflatalent.dto.response.report;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailedSaleReportDTO {
    private Long orderId;
    private LocalDate orderDate;
    private String companyName;
    private String customerName;
    private String itemDescription;
    private String businessLine;
    private String areaManager;
    private BigDecimal revenue;
    private BigDecimal cost;
    private BigDecimal profit;
}
