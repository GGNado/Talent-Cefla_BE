package com.giggi.ceflatalent.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.giggi.ceflatalent.entity.Sale;
import com.giggi.ceflatalent.repository.SaleRepository;
import com.giggi.ceflatalent.service.SaleService;

@Service
@Transactional
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;

    @Override
    public Sale save(Sale sale) {
        return saleRepository.save(sale);
    }

    @Override
    public Sale update(Sale sale) {
        return saleRepository.save(sale);
    }

    @Override
    public void deleteById(Long id) {
        saleRepository.deleteById(id);
    }

    @Override
    public List<Sale> findAll() {
        return saleRepository.findAll();
    }

    @Override
    public Sale findById(Long id) {
        return saleRepository.findById(id).orElse(null);
    }

    @Override
    public void printOrder(Long orderNum) {
        List<Sale> orderLines = saleRepository.findByOrderNum((orderNum));
        for (Sale line : orderLines) {
            System.out.println("Ordine: " + line.getOrderNum());
            System.out.println("Azienda: " + line.getCompany().getDescription());
            System.out.println("Cliente: " + line.getCustomer().getDescription());
            System.out.println("Ricavo: " + line.getRevenues());
            System.out.println("Costo: " + line.getCost());
            System.out.println("Prodotto: " + line.getItem().getDescription());
        }
    }
}