package com.giggi.ceflatalent.dto.response.report;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankingEntryDTO {
    private String identifier; // ID or Code
    private String name;       // Customer Name, Item Name, etc.
    private BigDecimal totalRevenue;
    private BigDecimal totalProfit;
    private Long count;        // Number of sales/orders
}
