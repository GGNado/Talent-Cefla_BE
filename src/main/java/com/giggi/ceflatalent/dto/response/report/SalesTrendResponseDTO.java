package com.giggi.ceflatalent.dto.response.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesTrendResponseDTO {
    private String interval; // "MONTHLY", "DAILY", "YEARLY"
    private List<TrendDataPointDTO> history;
    private List<TrendDataPointDTO> projections;
    private BigDecimal totalRevenueMean;
    private BigDecimal totalRevenueVariance;
    private BigDecimal totalRevenueStandardDeviation;
}
