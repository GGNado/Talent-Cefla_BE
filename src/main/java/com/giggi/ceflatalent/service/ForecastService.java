package com.giggi.ceflatalent.service;

import com.giggi.ceflatalent.dto.response.report.AdvancedForecastResponse;
import com.giggi.ceflatalent.dto.response.report.ChurnRiskEntry;
import com.giggi.ceflatalent.dto.response.report.MonteCarloEntry;
import com.giggi.ceflatalent.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ForecastService {

    private final SaleRepository saleRepository;
    private final Random random = new Random();

    public AdvancedForecastResponse getAdvancedForecast() {
        return AdvancedForecastResponse.builder()
                .monteCarlo(runMonteCarloSimulation())
                .churnRisk(predictChurnRisk())
                .build();
    }

    private List<MonteCarloEntry> runMonteCarloSimulation() {
        List<Object[]> trendData = saleRepository.findMonthlyTrendNative();
        if (trendData.isEmpty()) return new ArrayList<>();

        List<Double> revenues = trendData.stream()
                .map(row -> row[1] != null ? ((Number) row[1]).doubleValue() : 0.0)
                .collect(Collectors.toList());

        double mean = revenues.stream().mapToDouble(d -> d).average().orElse(0);
        double variance = revenues.stream().mapToDouble(d -> Math.pow(d - mean, 2)).average().orElse(0);
        double stdDev = Math.sqrt(variance);

        // Seasonality Indices (simplified)
        double[] seasonalIndices = new double[13];
        Arrays.fill(seasonalIndices, 1.0);
        double[] monthlySums = new double[13];
        int[] monthlyCounts = new int[13];

        for (Object[] row : trendData) {
            LocalDate date = parseLocalDate(row[0]);
            int month = date.getMonthValue();
            double rev = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
            monthlySums[month] += rev;
            monthlyCounts[month]++;
        }

        for (int m = 1; m <= 12; m++) {
            if (monthlyCounts[m] > 0 && mean > 0) {
                seasonalIndices[m] = (monthlySums[m] / monthlyCounts[m]) / mean;
            }
        }

        // Baseline: Avg of last 3 months
        double baseline = revenues.subList(Math.max(0, revenues.size() - 3), revenues.size())
                .stream().mapToDouble(d -> d).average().orElse(mean);

        List<MonteCarloEntry> results = new ArrayList<>();
        LocalDate lastDate = parseLocalDate(trendData.get(trendData.size() - 1)[0]);

        for (int i = 1; i <= 6; i++) {
            LocalDate targetDate = lastDate.plusMonths(i);
            int targetMonth = targetDate.getMonthValue();
            double monthlyBase = baseline * seasonalIndices[targetMonth];

            List<Double> simulations = new ArrayList<>();
            for (int s = 0; s < 1000; s++) {
                // Generate sample: Base + noise (stdDev)
                double sample = monthlyBase + (random.nextGaussian() * stdDev);
                simulations.add(Math.max(0, sample));
            }
            Collections.sort(simulations);

            results.add(MonteCarloEntry.builder()
                    .date(targetDate)
                    .revenueMin(BigDecimal.valueOf(simulations.get(50)).setScale(2, RoundingMode.HALF_UP)) // 5th percentile
                    .revenueMax(BigDecimal.valueOf(simulations.get(950)).setScale(2, RoundingMode.HALF_UP)) // 95th percentile
                    .revenueMedian(BigDecimal.valueOf(simulations.get(500)).setScale(2, RoundingMode.HALF_UP)) // Median
                    .build());
        }

        return results;
    }

    private List<ChurnRiskEntry> predictChurnRisk() {
        List<Object[]> data = saleRepository.findOrderDatesPerCustomer();
        Map<String, List<LocalDate>> customerOrders = new HashMap<>();

        for (Object[] row : data) {
            String name = (String) row[0];
            LocalDate date = parseLocalDate(row[1]);
            customerOrders.computeIfAbsent(name, k -> new ArrayList<>()).add(date);
        }

        List<ChurnRiskEntry> risks = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Map.Entry<String, List<LocalDate>> entry : customerOrders.entrySet()) {
            List<LocalDate> dates = entry.getValue();
            if (dates.size() < 3) continue;

            Collections.sort(dates);
            List<Long> intervals = new ArrayList<>();
            for (int i = 1; i < dates.size(); i++) {
                intervals.add(ChronoUnit.DAYS.between(dates.get(i - 1), dates.get(i)));
            }

            double avgInterval = intervals.stream().mapToLong(l -> l).average().orElse(0);
            LocalDate lastOrder = dates.get(dates.size() - 1);
            long daysSinceLastOrder = ChronoUnit.DAYS.between(lastOrder, today);

            // Churn logic: gap > avgInterval * 1.5
            if (daysSinceLastOrder > avgInterval * 1.5) {
                // Risk score: (daysSinceLastOrder / (avgInterval * 3)) capped at 1.0
                double riskScore = Math.min(1.0, (double) daysSinceLastOrder / (avgInterval * 3));
                
                risks.add(ChurnRiskEntry.builder()
                        .customerName(entry.getKey())
                        .riskScore(riskScore)
                        .lastOrderDate(lastOrder)
                        .build());
            }
        }

        risks.sort((a, b) -> b.getRiskScore().compareTo(a.getRiskScore()));
        return risks;
    }

    private LocalDate parseLocalDate(Object dateVal) {
        if (dateVal instanceof java.sql.Timestamp ts) {
            return ts.toLocalDateTime().toLocalDate();
        } else if (dateVal instanceof java.time.Instant instant) {
            return instant.atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        } else if (dateVal instanceof java.time.LocalDateTime ldt) {
            return ldt.toLocalDate();
        } else if (dateVal instanceof java.time.LocalDate ld) {
            return ld;
        } else {
            return LocalDate.parse(dateVal.toString().substring(0, 10));
        }
    }
}
