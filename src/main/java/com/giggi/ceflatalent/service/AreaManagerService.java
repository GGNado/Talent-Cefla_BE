package com.giggi.ceflatalent.service;

import java.util.List;

import com.giggi.ceflatalent.entity.AreaManager;

public interface AreaManagerService {
    AreaManager save(AreaManager areaManager);

    AreaManager update(AreaManager areaManager);

    void deleteById(Long id);

    List<AreaManager> findAll();

    AreaManager findById(Long id);
}