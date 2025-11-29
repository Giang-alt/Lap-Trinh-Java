package com.evmarketplace.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.evmarketplace.entity.DataSource;
import com.evmarketplace.entity.DataProvider;
import com.evmarketplace.entity.User;
import com.evmarketplace.repository.DataSourceRepository;
import com.evmarketplace.service.UserService;

@RestController
@RequestMapping("/data-sources")
@CrossOrigin(origins = "http://localhost:3000")
public class DataSourceController {
    
    @Autowired
    private DataSourceRepository dataSourceRepository;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/by-provider/{providerId}")
    @PreAuthorize("hasAnyRole('DATA_PROVIDER', 'ADMIN')")
    public ResponseEntity<List<DataSource>> getDataSourcesByProvider(@PathVariable Long providerId) {
        // Kiểm tra quyền truy cập
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        if (currentUser.getRole() == User.UserRole.DATA_PROVIDER) {
            if (!currentUser.getId().equals(providerId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(List.of());
            }
        }
        
        List<DataSource> dataSources = dataSourceRepository.findByDataProviderId(providerId);
        return ResponseEntity.ok(dataSources);
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('DATA_PROVIDER', 'ADMIN')")
    public ResponseEntity<?> createDataSource(@RequestBody DataSource dataSource) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) auth.getPrincipal();
            
            // Lấy full user entity để có thông tin provider
            User fullUser = userService.findByUsername(currentUser.getUsername())
                    .orElseThrow(() -> new IllegalStateException("User not found"));
            
            if (!(fullUser instanceof DataProvider)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Only data providers can create data sources"));
            }
            
            DataProvider provider = (DataProvider) fullUser;
            dataSource.setDataProvider(provider);
            
            DataSource created = dataSourceRepository.save(dataSource);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create data source: " + e.getMessage()));
        }
    }
    
    @PostMapping("/auto-create")
    @PreAuthorize("hasAnyRole('DATA_PROVIDER', 'ADMIN')")
    public ResponseEntity<?> autoCreateDefaultDataSource() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) auth.getPrincipal();
            
            // Lấy full user entity
            User fullUser = userService.findByUsername(currentUser.getUsername())
                    .orElseThrow(() -> new IllegalStateException("User not found"));
            
            if (!(fullUser instanceof DataProvider)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Only data providers can create data sources"));
            }
            
            DataProvider provider = (DataProvider) fullUser;
            
            // Kiểm tra xem đã có data source chưa
            List<DataSource> existing = dataSourceRepository.findByDataProviderId(provider.getId());
            if (!existing.isEmpty()) {
                return ResponseEntity.ok(existing.get(0)); // Trả về data source đầu tiên
            }
            
            // Tạo data source mặc định
            DataSource defaultSource = new DataSource();
            defaultSource.setName("Nguồn dữ liệu mặc định của " + provider.getCompanyName());
            defaultSource.setType(DataSource.DataSourceType.BATTERY_DATA);
            defaultSource.setDescription("Nguồn dữ liệu được tạo tự động");
            defaultSource.setDataProvider(provider);
            defaultSource.setStatus(DataSource.DataSourceStatus.ACTIVE);
            
            DataSource created = dataSourceRepository.save(defaultSource);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create data source: " + e.getMessage()));
        }
    }
}

