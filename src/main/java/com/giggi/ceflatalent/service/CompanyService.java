package com.giggi.ceflatalent.service;

import java.util.List;

import com.giggi.ceflatalent.entity.Company;

public interface CompanyService {
    Company save(Company company);

    Company update(Company company);

    void deleteById(Long id);

    List<Company> findAll();

    Company findById(Long id);
}