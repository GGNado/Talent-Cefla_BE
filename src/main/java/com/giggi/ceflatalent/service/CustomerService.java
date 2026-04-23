package com.giggi.ceflatalent.service;

import java.util.List;

import com.giggi.ceflatalent.entity.Customer;

public interface CustomerService {
    Customer save(Customer customer);

    Customer update(Customer customer);

    void deleteById(Long id);

    List<Customer> findAll();

    Customer findById(Long id);
}