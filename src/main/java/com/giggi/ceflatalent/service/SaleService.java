package com.giggi.ceflatalent.service;

import java.util.List;

import com.giggi.ceflatalent.entity.Sale;

public interface SaleService {
    Sale save(Sale sale);

    Sale update(Sale sale);

    void deleteById(Long id);

    List<Sale> findAll();

    Sale findById(Long id);

    @Deprecated
    void printOrder(Long orederNum);
}