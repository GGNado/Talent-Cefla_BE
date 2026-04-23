package com.giggi.ceflatalent.service;

import com.giggi.ceflatalent.dto.response.report.*;
import com.giggi.ceflatalent.entity.ReportType;
import com.giggi.ceflatalent.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final SaleRepository saleRepository;

    public TopPerformersResponseDTO getTopPerformers(ReportType type) {
        List<RankingEntryDTO> entries = switch (type) {
            case TOP_CUSTOMERS -> saleRepository.findTopCustomers();
            case TOP_ITEMS -> saleRepository.findTopItems();
            case TOP_AREA_MANAGERS -> saleRepository.findTopAreaManagers();
        };

        return TopPerformersResponseDTO.builder()
                .reportType(type)
                .entries(entries)
                .build();
    }

    public SalesTrendResponseDTO getSalesTrend() {
        List<Object[]> trendData = saleRepository.findMonthlyTrendNative();
        List<TrendDataPointDTO> history = mapToTrendDataPoints(trendData);

        if (history.isEmpty()) {
            return SalesTrendResponseDTO.builder().history(history).projections(new ArrayList<>()).build();
        }

        // Stats calculation
        double n = history.size();
        double sumY = history.stream().mapToDouble(p -> p.getRevenue().doubleValue()).sum();
        double mean = sumY / n;
        
        double sumSqDiff = history.stream()
                .mapToDouble(p -> Math.pow(p.getRevenue().doubleValue() - mean, 2))
                .sum();
        double variance = sumSqDiff / n;

        // Linear Regression (y = a + bx)
        double sumX = 0;
        double sumXY = 0;
        double sumX2 = 0;
        for (int i = 0; i < n; i++) {
            double y = history.get(i).getRevenue().doubleValue();
            sumX += i;
            sumXY += i * y;
            sumX2 += i * i;
        }
        
        double b = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
        double a = (sumY - b * sumX) / n;
        double standardDeviation = Math.sqrt(variance);

        // Cost ratio for projecting costs
        double sumCost = history.stream().mapToDouble(p -> p.getCost().doubleValue()).sum();
        double costRatio = sumY > 0 ? sumCost / sumY : 0.5;

        List<TrendDataPointDTO> projections = new ArrayList<>();
        LocalDate lastDate = history.get(history.size() - 1).getDate();
        for (int i = 1; i <= 6; i++) {
            double x = n + i - 1;
            double projectedRev = Math.max(0, a + b * x);
            double projectedCost = projectedRev * costRatio;
            
            projections.add(TrendDataPointDTO.builder()
                    .date(lastDate.plusMonths(i))
                    .revenue(BigDecimal.valueOf(projectedRev).setScale(2, RoundingMode.HALF_UP))
                    .cost(BigDecimal.valueOf(projectedCost).setScale(2, RoundingMode.HALF_UP))
                    .profit(BigDecimal.valueOf(projectedRev - projectedCost).setScale(2, RoundingMode.HALF_UP))
                    .projectedRevenue(BigDecimal.valueOf(projectedRev).setScale(2, RoundingMode.HALF_UP))
                    .projection(true)
                    .build());
        }

        return SalesTrendResponseDTO.builder()
                .interval("MONTHLY")
                .history(history)
                .projections(projections)
                .totalRevenueMean(BigDecimal.valueOf(mean).setScale(2, RoundingMode.HALF_UP))
                .totalRevenueVariance(BigDecimal.valueOf(variance).setScale(2, RoundingMode.HALF_UP))
                .totalRevenueStandardDeviation(BigDecimal.valueOf(standardDeviation).setScale(2, RoundingMode.HALF_UP))
                .build();
    }

    public SalesTrendResponseDTO getSeasonalSalesTrend() {
        List<Object[]> trendData = saleRepository.findMonthlyTrendNative();
        List<TrendDataPointDTO> history = mapToTrendDataPoints(trendData);

        if (history.size() < 12) {
            // Fallback to linear regression if not enough data for seasonality
            return getSalesTrend();
        }

        // 1. Calculate Seasonality Indices
        double globalAvg = history.stream().mapToDouble(p -> p.getRevenue().doubleValue()).average().orElse(0);
        if (globalAvg == 0) return getSalesTrend();

        double[] monthlySums = new double[13];
        int[] monthlyCounts = new int[13];
        for (TrendDataPointDTO p : history) {
            int month = p.getDate().getMonthValue();
            monthlySums[month] += p.getRevenue().doubleValue();
            monthlyCounts[month]++;
        }

        double[] seasonalIndices = new double[13];
        for (int m = 1; m <= 12; m++) {
            if (monthlyCounts[m] > 0) {
                double monthAvg = monthlySums[m] / monthlyCounts[m];
                seasonalIndices[m] = monthAvg / globalAvg;
            } else {
                seasonalIndices[m] = 1.0; // No data for this month, assume average
            }
        }

        // 2. Baseline calculation (Average of last 3 months)
        double baseline = history.subList(Math.max(0, history.size() - 3), history.size())
                .stream().mapToDouble(p -> p.getRevenue().doubleValue())
                .average().orElse(globalAvg);

        // 3. Projection for next 6 months
        double sumCost = history.stream().mapToDouble(p -> p.getCost().doubleValue()).sum();
        double sumRev = history.stream().mapToDouble(p -> p.getRevenue().doubleValue()).sum();
        double costRatio = sumRev > 0 ? sumCost / sumRev : 0.5;

        List<TrendDataPointDTO> projections = new ArrayList<>();
        LocalDate lastDate = history.get(history.size() - 1).getDate();
        for (int i = 1; i <= 6; i++) {
            LocalDate targetDate = lastDate.plusMonths(i);
            int targetMonth = targetDate.getMonthValue();
            double projectedRev = baseline * seasonalIndices[targetMonth];
            double projectedCost = projectedRev * costRatio;

            projections.add(TrendDataPointDTO.builder()
                    .date(targetDate)
                    .revenue(BigDecimal.valueOf(projectedRev).setScale(2, RoundingMode.HALF_UP))
                    .cost(BigDecimal.valueOf(projectedCost).setScale(2, RoundingMode.HALF_UP))
                    .profit(BigDecimal.valueOf(projectedRev - projectedCost).setScale(2, RoundingMode.HALF_UP))
                    .projectedRevenue(BigDecimal.valueOf(projectedRev).setScale(2, RoundingMode.HALF_UP))
                    .projection(true)
                    .build());
        }

        // Stats for response (reusing some from history)
        double n = history.size();
        double sumSqDiff = history.stream()
                .mapToDouble(p -> Math.pow(p.getRevenue().doubleValue() - globalAvg, 2))
                .sum();
        double variance = sumSqDiff / n;
        double standardDeviation = Math.sqrt(variance);

        return SalesTrendResponseDTO.builder()
                .interval("MONTHLY_SEASONAL")
                .history(history)
                .projections(projections)
                .totalRevenueMean(BigDecimal.valueOf(globalAvg).setScale(2, RoundingMode.HALF_UP))
                .totalRevenueVariance(BigDecimal.valueOf(variance).setScale(2, RoundingMode.HALF_UP))
                .totalRevenueStandardDeviation(BigDecimal.valueOf(standardDeviation).setScale(2, RoundingMode.HALF_UP))
                .build();
    }

    private List<TrendDataPointDTO> mapToTrendDataPoints(List<Object[]> trendData) {
        return trendData.stream().map(row -> {
            Object dateVal = row[0];
            LocalDate date;
            if (dateVal instanceof java.sql.Timestamp ts) {
                date = ts.toLocalDateTime().toLocalDate();
            } else if (dateVal instanceof java.time.Instant instant) {
                date = instant.atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            } else if (dateVal instanceof java.time.LocalDateTime ldt) {
                date = ldt.toLocalDate();
            } else {
                date = LocalDate.parse(dateVal.toString().substring(0, 10));
            }

            return TrendDataPointDTO.builder()
                    .date(date)
                    .revenue(row[1] != null ? BigDecimal.valueOf(((Number) row[1]).doubleValue()) : BigDecimal.ZERO)
                    .cost(row[2] != null ? BigDecimal.valueOf(((Number) row[2]).doubleValue()) : BigDecimal.ZERO)
                    .profit(row[3] != null ? BigDecimal.valueOf(((Number) row[3]).doubleValue()) : BigDecimal.ZERO)
                    .projection(false)
                    .build();
        }).collect(Collectors.toList());
    }

    public GlobalSummaryResponseDTO getGlobalSummary() {
        Object raw = saleRepository.getGlobalSummaryRaw();
        Object[] row = (Object[]) raw;
        return GlobalSummaryResponseDTO.builder()
                .totalRevenue(row[0] != null ? BigDecimal.valueOf(((Number) row[0]).doubleValue()) : BigDecimal.ZERO)
                .totalCost(row[1] != null ? BigDecimal.valueOf(((Number) row[1]).doubleValue()) : BigDecimal.ZERO)
                .totalProfit(row[2] != null ? BigDecimal.valueOf(((Number) row[2]).doubleValue()) : BigDecimal.ZERO)
                .averageMargin(row[3] != null ? ((Number) row[3]).doubleValue() : 0.0)
                .totalOrders(row[4] != null ? ((Number) row[4]).longValue() : 0L)
                .uniqueCustomers(row[5] != null ? ((Number) row[5]).longValue() : 0L)
                .uniqueItems(row[6] != null ? ((Number) row[6]).longValue() : 0L)
                .build();
    }

    public List<DetailedSaleReportDTO> getDetailedSales() {
        return saleRepository.findAll().stream().map(sale -> DetailedSaleReportDTO.builder()
                .orderId(sale.getId())
                .orderDate(sale.getOrderDate())
                .companyName(sale.getCompany().getDescription())
                .customerName(sale.getCustomer().getDescription())
                .itemDescription(sale.getItem().getDescription())
                .businessLine(sale.getItem().getBusinessLine().getDescription())
                .areaManager(sale.getCustomer().getAreaManager().getDescription())
                .revenue(sale.getRevenues())
                .cost(sale.getCost())
                .profit(sale.getRevenues().subtract(sale.getCost()))
                .build()).collect(Collectors.toList());
    }
}
