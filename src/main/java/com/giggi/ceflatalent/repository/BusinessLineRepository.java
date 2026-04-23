package com.giggi.ceflatalent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.giggi.ceflatalent.entity.BusinessLine;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessLineRepository extends JpaRepository<BusinessLine, Long> {
}