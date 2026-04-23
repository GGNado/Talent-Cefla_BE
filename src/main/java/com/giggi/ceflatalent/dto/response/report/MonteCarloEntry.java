package com.giggi.ceflatalent.dto.response.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonteCarloEntry {
    private LocalDate date;
    private BigDecimal revenueMin;
    private BigDecimal revenueMax;
    private BigDecimal revenueMedian;
}
