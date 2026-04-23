package com.giggi.ceflatalent.controller;

import com.giggi.ceflatalent.dto.response.report.AdvancedForecastResponse;
import com.giggi.ceflatalent.service.ForecastService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports/forecast")
@RequiredArgsConstructor
public class ForecastController {

    private final ForecastService forecastService;

    @GetMapping("/advanced")
    public AdvancedForecastResponse getAdvancedForecast() {
        return forecastService.getAdvancedForecast();
    }
}
