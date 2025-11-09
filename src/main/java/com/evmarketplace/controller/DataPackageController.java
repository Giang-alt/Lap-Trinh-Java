package com.evmarketplace.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.evmarketplace.entity.DataPackage;
import com.evmarketplace.service.DataPackageService;

/**
 * Controller xử lý các API liên quan đến Data Package
 *
 * API của Giang:
 * - GET /data-packages - Lấy tất cả data packages
 */
@RestController
@RequestMapping("/data-packages")
@CrossOrigin(origins = "http://localhost:3000")
public class DataPackageController {

    @Autowired
    private DataPackageService dataPackageService;

    /**
     * API: GET /data-packages
     * Mô tả: Lấy danh sách tất cả data packages
     * Response: 200 OK với danh sách DataPackage
     */
    @GetMapping
    public ResponseEntity<List<DataPackage>> getAllDataPackages() {
        List<DataPackage> packages = dataPackageService.findAll();
        return ResponseEntity.ok(packages);
    }
    @GetMapping("/{id}")
    public ResponseEntity<DataPackage> getDataPackageById(@PathVariable Long id) {
        Optional<DataPackage> dataPackage = dataPackageService.findById(id);

        if (dataPackage.isPresent()) {
            return ResponseEntity.ok(dataPackage.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
