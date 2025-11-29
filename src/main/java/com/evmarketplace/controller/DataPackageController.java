package com.evmarketplace.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.evmarketplace.entity.DataPackage;
import com.evmarketplace.entity.User;
import com.evmarketplace.entity.DataProvider;
import com.evmarketplace.service.DataPackageService;
import com.evmarketplace.service.UserService;
import com.evmarketplace.dto.UpdateDataPackageRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/data-packages")
@CrossOrigin(origins = "http://localhost:3000")
public class DataPackageController {
    
    @Autowired
    private DataPackageService dataPackageService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('DATA_CONSUMER', 'ADMIN')")
    public ResponseEntity<List<DataPackage>> getAllDataPackages() {
        List<DataPackage> packages = dataPackageService.findAll();
        return ResponseEntity.ok(packages);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DATA_CONSUMER', 'ADMIN')")
    public ResponseEntity<DataPackage> getDataPackageById(@PathVariable Long id) {
        return dataPackageService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('DATA_CONSUMER', 'ADMIN')")
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
                // ignore invalid type
            }
        }
        
        DataPackage.DataFormat dataFormat = null;
        if (format != null && !format.trim().isEmpty()) {
            try {
                dataFormat = DataPackage.DataFormat.valueOf(format.toUpperCase());
            } catch (IllegalArgumentException e) {
                // ignore invalid format
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
    @PreAuthorize("hasAnyRole('DATA_PROVIDER', 'ADMIN')")
    public ResponseEntity<List<DataPackage>> getDataPackagesByProvider(@PathVariable Long dataProviderId) {
        // Kiểm tra Provider chỉ xem được gói của chính mình
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        if (currentUser.getRole() == User.UserRole.DATA_PROVIDER) {
            // Lấy provider ID từ current user
            Long currentProviderId = getCurrentProviderId(currentUser);
            if (currentProviderId == null || !dataProviderId.equals(currentProviderId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(List.of());
            }
        }
        
        List<DataPackage> packages = dataPackageService.findByDataProviderId(dataProviderId);
        return ResponseEntity.ok(packages);
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('DATA_PROVIDER', 'ADMIN')")
    public ResponseEntity<?> createDataPackage(@RequestBody DataPackage dataPackage) {
        try {
            DataPackage createdPackage = dataPackageService.createDataPackage(dataPackage);
            return ResponseEntity.ok(createdPackage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (jakarta.persistence.EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('DATA_PROVIDER', 'ADMIN')")
    public ResponseEntity<DataPackage> updateDataPackage(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDataPackageRequest request) {
        // Kiểm tra Provider chỉ sửa được gói của chính mình
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        if (currentUser.getRole() == User.UserRole.DATA_PROVIDER) {
            if (!isOwnerOfDataPackage(id, currentUser)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        
        return dataPackageService.updateDataPackage(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('DATA_PROVIDER', 'ADMIN')")
    public ResponseEntity<?> deleteDataPackage(@PathVariable Long id) {
        // Kiểm tra Provider chỉ xóa được gói của chính mình
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        if (currentUser.getRole() == User.UserRole.DATA_PROVIDER) {
            if (!isOwnerOfDataPackage(id, currentUser)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "You can only delete your own data packages"));
            }
        }
        
        try {
            dataPackageService.deleteDataPackage(id);
            Map<String, String> response = Map.of("message", "Data package deleted successfully");
            return ResponseEntity.ok(response);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete data package: " + e.getMessage()));
        }
    }
    
    // Helper methods
    private Long getCurrentProviderId(User user) {
        // Lấy full user entity từ database để có thông tin provider
        Optional<User> fullUser = userService.findByUsername(user.getUsername());
        if (fullUser.isPresent() && fullUser.get() instanceof DataProvider) {
            return fullUser.get().getId();
        }
        return null;
    }
    
    private boolean isOwnerOfDataPackage(Long packageId, User user) {
        if (user.getRole() != User.UserRole.DATA_PROVIDER) {
            return false;
        }
        
        Optional<DataPackage> dataPackage = dataPackageService.findById(packageId);
        if (dataPackage.isEmpty()) {
            return false;
        }
        
        DataPackage pkg = dataPackage.get();
        if (pkg.getDataSource() == null || pkg.getDataSource().getDataProvider() == null) {
            return false;
        }
        
        Long providerId = pkg.getDataSource().getDataProvider().getId();
        Long currentProviderId = getCurrentProviderId(user);
        
        return providerId != null && currentProviderId != null && providerId.equals(currentProviderId);
    }
}
