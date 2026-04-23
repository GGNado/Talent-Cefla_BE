package com.giggi.ceflatalent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.giggi.ceflatalent.entity.AreaManager;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaManagerRepository extends JpaRepository<AreaManager, Long> {
}