package com.evmarketplace.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evmarketplace.entity.DataPackage;
import com.evmarketplace.repository.DataPackageRepository;

@Service
public class DataPackageService {
    
    @Autowired
    private DataPackageRepository dataPackageRepository;
    
    public DataPackage createDataPackage(DataPackage dataPackage) {
        return dataPackageRepository.save(dataPackage);
    }
    
    public Optional<DataPackage> findById(Long id) {
        return dataPackageRepository.findById(id);
    }
    
    public List<DataPackage> findAll() {
        return dataPackageRepository.findAll();
    }
    
    public List<DataPackage> findByDataSourceId(Long dataSourceId) {
        return dataPackageRepository.findByDataSourceId(dataSourceId);
    }
    
    public List<DataPackage> findByDataType(DataPackage.DataType dataType) {
        return dataPackageRepository.findByDataType(dataType);
    }
    
    public List<DataPackage> findByFormat(DataPackage.DataFormat format) {
        return dataPackageRepository.findByFormat(format);
    }
    
    public List<DataPackage> findByStatus(DataPackage.PackageStatus status) {
        return dataPackageRepository.findByStatus(status);
    }
    
    public List<DataPackage> findByNameContaining(String name) {
        return dataPackageRepository.findByNameContainingIgnoreCase(name);
    }
    
    public List<DataPackage> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return dataPackageRepository.findByPriceRange(minPrice, maxPrice);
    }
    
    public List<DataPackage> findByDataProviderId(Long dataProviderId) {
        return dataPackageRepository.findByDataProviderId(dataProviderId);
    }
    
    public DataPackage updateDataPackage(DataPackage dataPackage) {
        return dataPackageRepository.save(dataPackage);
    }
    
    public void deleteDataPackage(Long id) {
        dataPackageRepository.deleteById(id);
    }
    
    public List<DataPackage> searchDataPackages(String name, DataPackage.DataType dataType, 
                                               DataPackage.DataFormat format, BigDecimal minPrice, BigDecimal maxPrice) {
        List<DataPackage> results = findAll();
        
        if (name != null && !name.trim().isEmpty()) {
            results = results.stream()
                    .filter(dp -> dp.getName().toLowerCase().contains(name.toLowerCase()))
                    .toList();
        }
        
        if (dataType != null) {
            results = results.stream()
                    .filter(dp -> dp.getDataType() == dataType)
                    .toList();
        }
        
        if (format != null) {
            results = results.stream()
                    .filter(dp -> dp.getFormat() == format)
                    .toList();
        }
        
        if (minPrice != null) {
            results = results.stream()
                    .filter(dp -> dp.getPrice().compareTo(minPrice) >= 0)
                    .toList();
        }
        
        if (maxPrice != null) {
            results = results.stream()
                    .filter(dp -> dp.getPrice().compareTo(maxPrice) <= 0)
                    .toList();
        }
        
        return results;
    }
}
