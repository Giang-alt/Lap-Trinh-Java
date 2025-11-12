package com.evmarketplace.controller;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.evmarketplace.entity.DataPackage;
import com.evmarketplace.entity.DataPackage.DataFormat;
import com.evmarketplace.entity.DataPackage.DataType;
import com.evmarketplace.entity.DataPackage.PackageStatus;
import com.evmarketplace.entity.DataPackage.PricingModel;
import com.evmarketplace.service.DataPackageService;

@RestController
@RequestMapping("/data-packages")
@CrossOrigin(origins = "http://localhost:3000")
public class DataPackageController {

    @Autowired
    private DataPackageService dataPackageService;

    @GetMapping
    public ResponseEntity<List<DataPackage>> getAllDataPackages() {
        return ResponseEntity.ok(dataPackageService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataPackage> getDataPackageById(@PathVariable Long id) {
        return dataPackageService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<DataPackage>> searchDataPackages(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) DataType dataType,
            @RequestParam(required = false) DataFormat format,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) PackageStatus status,
            @RequestParam(required = false) PricingModel pricingModel,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") 
                ? Sort.Direction.DESC 
                : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<DataPackage> results = dataPackageService.searchDataPackages(
                name, dataType, format, minPrice, maxPrice, status, pricingModel, pageable
        );

        return ResponseEntity.ok(results);
    }
}
