package com.giggi.ceflatalent.dto.response.report;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GlobalSummaryResponseDTO {
    private BigDecimal totalRevenue;
    private BigDecimal totalCost;
    private BigDecimal totalProfit;
    private Double averageMargin;
    private Long totalOrders;
    private Long uniqueCustomers;
    private Long uniqueItems;
}
