package com.giggi.ceflatalent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.giggi.ceflatalent.entity.Company;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
}