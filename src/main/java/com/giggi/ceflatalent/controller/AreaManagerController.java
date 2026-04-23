package com.giggi.ceflatalent.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.giggi.ceflatalent.entity.AreaManager;
import com.giggi.ceflatalent.service.AreaManagerService;

@RestController
@RequestMapping("/api/areaManagers")
@RequiredArgsConstructor
public class AreaManagerController {
    private final AreaManagerService areaManagerService;

    @GetMapping
    public List<AreaManager> getAllAreaManagers() {
        return areaManagerService.findAll();
    }
    // CRUD endpoints qui
}