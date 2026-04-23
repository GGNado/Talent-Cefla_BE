package com.giggi.ceflatalent.service;

import java.util.List;

import com.giggi.ceflatalent.entity.Item;

public interface ItemService {
    Item save(Item item);

    Item update(Item item);

    void deleteById(Long id);

    List<Item> findAll();

    Item findById(Long id);
}