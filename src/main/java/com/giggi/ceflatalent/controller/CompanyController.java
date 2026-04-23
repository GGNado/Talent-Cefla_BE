package com.giggi.ceflatalent.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.giggi.ceflatalent.entity.Company;
import com.giggi.ceflatalent.service.CompanyService;

@RestController
@RequestMapping("/api/companys")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping
    public List<Company> getAllCompanys() {
        return companyService.findAll();
    }
    // CRUD endpoints qui
}