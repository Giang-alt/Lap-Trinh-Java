package com.evmarketplace.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
