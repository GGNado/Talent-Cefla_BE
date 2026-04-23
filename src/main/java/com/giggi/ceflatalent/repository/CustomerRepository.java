package com.giggi.ceflatalent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.giggi.ceflatalent.entity.Customer;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}