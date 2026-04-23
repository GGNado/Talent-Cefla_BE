package com.giggi.ceflatalent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.giggi.ceflatalent.entity.Item;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}