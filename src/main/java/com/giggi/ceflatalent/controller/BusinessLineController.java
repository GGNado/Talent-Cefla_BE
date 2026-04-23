package com.giggi.ceflatalent.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.giggi.ceflatalent.entity.BusinessLine;
import com.giggi.ceflatalent.service.BusinessLineService;

@RestController
@RequestMapping("/api/businessLines")
@RequiredArgsConstructor
public class BusinessLineController {
    private final BusinessLineService businessLineService;

    @GetMapping
    public List<BusinessLine> getAllBusinessLines() {
        return businessLineService.findAll();
    }
    // CRUD endpoints qui
}