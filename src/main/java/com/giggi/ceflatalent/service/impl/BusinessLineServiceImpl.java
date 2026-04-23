package com.giggi.ceflatalent.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.giggi.ceflatalent.entity.BusinessLine;
import com.giggi.ceflatalent.repository.BusinessLineRepository;
import com.giggi.ceflatalent.service.BusinessLineService;

@Service
@Transactional
@RequiredArgsConstructor
public class BusinessLineServiceImpl implements BusinessLineService {

    private final BusinessLineRepository businessLineRepository;

    @Override
    public BusinessLine save(BusinessLine businessLine) {
        return businessLineRepository.save(businessLine);
    }

    @Override
    public BusinessLine update(BusinessLine businessLine) {
        return businessLineRepository.save(businessLine);
    }

    @Override
    public void deleteById(Long id) {
        businessLineRepository.deleteById(id);
    }

    @Override
    public List<BusinessLine> findAll() {
        return businessLineRepository.findAll();
    }

    @Override
    public BusinessLine findById(Long id) {
        return businessLineRepository.findById(id).orElse(null);
    }
}