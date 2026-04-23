package com.giggi.ceflatalent.dto.response.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChurnRiskEntry {
    private String customerName;
    private Double riskScore;
    private LocalDate lastOrderDate;
}
