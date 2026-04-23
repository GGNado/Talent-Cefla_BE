package com.giggi.ceflatalent.dto.response.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvancedForecastResponse {
    private List<MonteCarloEntry> monteCarlo;
    private List<ChurnRiskEntry> churnRisk;
}
