package com.evmarketplace.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.evmarketplace.entity.DataPackage;
import com.evmarketplace.service.DataPackageService;

@RestController
@RequestMapping("/data-packages")
@CrossOrigin(origins = "http://localhost:3000")
public class DataPackageController {
    
    @Autowired
    private DataPackageService dataPackageService;
    
    @GetMapping
    public ResponseEntity<List<DataPackage>> getAllDataPackages() {
        List<DataPackage> packages = dataPackageService.findAll();
        return ResponseEntity.ok(packages);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DataPackage> getDataPackageById(@PathVariable Long id) {
        return dataPackageService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<DataPackage>> searchDataPackages(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String dataType,
            @RequestParam(required = false) String format,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        
        DataPackage.DataType type = null;
        if (dataType != null && !dataType.trim().isEmpty()) {
            try {
                type = DataPackage.DataType.valueOf(dataType.toUpperCase());
            } catch (IllegalArgumentException e) {
            }
        }
        
        DataPackage.DataFormat dataFormat = null;
        if (format != null && !format.trim().isEmpty()) {
            try {
                dataFormat = DataPackage.DataFormat.valueOf(format.toUpperCase());
            } catch (IllegalArgumentException e) {
            }
        }
        
        List<DataPackage> packages = dataPackageService.searchDataPackages(name, type, dataFormat, minPrice, maxPrice);
        return ResponseEntity.ok(packages);
    }
    
    @GetMapping("/by-source/{dataSourceId}")
    public ResponseEntity<List<DataPackage>> getDataPackagesBySource(@PathVariable Long dataSourceId) {
        List<DataPackage> packages = dataPackageService.findByDataSourceId(dataSourceId);
        return ResponseEntity.ok(packages);
    }
    
    @GetMapping("/by-provider/{dataProviderId}")
    public ResponseEntity<List<DataPackage>> getDataPackagesByProvider(@PathVariable Long dataProviderId) {
        List<DataPackage> packages = dataPackageService.findByDataProviderId(dataProviderId);
        return ResponseEntity.ok(packages);
    }
    
    @PostMapping
    public ResponseEntity<DataPackage> createDataPackage(@RequestBody DataPackage dataPackage) {
        DataPackage createdPackage = dataPackageService.createDataPackage(dataPackage);
        return ResponseEntity.ok(createdPackage);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<DataPackage> updateDataPackage(@PathVariable Long id, @RequestBody DataPackage dataPackage) {
        dataPackage.setId(id);
        DataPackage updatedPackage = dataPackageService.updateDataPackage(dataPackage);
        return ResponseEntity.ok(updatedPackage);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteDataPackage(@PathVariable Long id) {
        dataPackageService.deleteDataPackage(id);
        Map<String, String> response = Map.of("message", "Data package deleted successfully");
        return ResponseEntity.ok(response);
    }
}
