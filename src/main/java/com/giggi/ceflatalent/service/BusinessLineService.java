package com.giggi.ceflatalent.service;

import java.util.List;

import com.giggi.ceflatalent.entity.BusinessLine;

public interface BusinessLineService {
    BusinessLine save(BusinessLine businessLine);

    BusinessLine update(BusinessLine businessLine);

    void deleteById(Long id);

    List<BusinessLine> findAll();

    BusinessLine findById(Long id);
}