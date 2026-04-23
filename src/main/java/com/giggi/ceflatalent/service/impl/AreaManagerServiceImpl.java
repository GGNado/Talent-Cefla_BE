package com.giggi.ceflatalent.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.giggi.ceflatalent.entity.AreaManager;
import com.giggi.ceflatalent.repository.AreaManagerRepository;
import com.giggi.ceflatalent.service.AreaManagerService;

@Service
@Transactional
@RequiredArgsConstructor
public class AreaManagerServiceImpl implements AreaManagerService {

    private final AreaManagerRepository areaManagerRepository;

    @Override
    public AreaManager save(AreaManager areaManager) {
        return areaManagerRepository.save(areaManager);
    }

    @Override
    public AreaManager update(AreaManager areaManager) {
        return areaManagerRepository.save(areaManager);
    }

    @Override
    public void deleteById(Long id) {
        areaManagerRepository.deleteById(id);
    }

    @Override
    public List<AreaManager> findAll() {
        return areaManagerRepository.findAll();
    }

    @Override
    public AreaManager findById(Long id) {
        return areaManagerRepository.findById(id).orElse(null);
    }
}