package com.giggi.ceflatalent.controller;

import com.giggi.ceflatalent.dto.response.report.GlobalSummaryResponseDTO;
import com.giggi.ceflatalent.dto.response.report.SalesTrendResponseDTO;
import com.giggi.ceflatalent.dto.response.report.TopPerformersResponseDTO;
import com.giggi.ceflatalent.dto.response.report.DetailedSaleReportDTO;
import com.giggi.ceflatalent.entity.ReportType;
import com.giggi.ceflatalent.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/top-performers/{type}")
    public TopPerformersResponseDTO getTopPerformers(@PathVariable ReportType type) {
        return reportService.getTopPerformers(type);
    }

    @GetMapping("/trend")
    public SalesTrendResponseDTO getSalesTrend() {
        return reportService.getSalesTrend();
    }

    @GetMapping("/trend/seasonal")
    public SalesTrendResponseDTO getSeasonalSalesTrend() {
        return reportService.getSeasonalSalesTrend();
    }

    @GetMapping("/summary")
    public GlobalSummaryResponseDTO getGlobalSummary() {
        return reportService.getGlobalSummary();
    }

    @GetMapping("/detailed")
    public List<DetailedSaleReportDTO> getDetailedSales() {
        return reportService.getDetailedSales();
    }
}
