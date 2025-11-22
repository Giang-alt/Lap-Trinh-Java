package com.evmarketplace.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Thêm import này

import com.evmarketplace.entity.DataPackage;
import com.evmarketplace.entity.DataSource;
import com.evmarketplace.repository.DataPackageRepository;
import com.evmarketplace.repository.DataSourceRepository;
import com.evmarketplace.dto.UpdateDataPackageRequest;
import jakarta.persistence.EntityNotFoundException; // Đã có sẵn

@Service
public class DataPackageService {
    
    @Autowired
    private DataPackageRepository dataPackageRepository;
    
    @Autowired
    private DataSourceRepository dataSourceRepository;
    
    public DataPackage createDataPackage(DataPackage dataPackage) {
        if (dataPackage.getDataSource() == null || dataPackage.getDataSource().getId() == null) {
            throw new IllegalArgumentException("Data source is required");
        }
        
        DataSource dataSource = dataSourceRepository.findById(dataPackage.getDataSource().getId())
                .orElseThrow(() -> new EntityNotFoundException("Data source not found"));
        
        dataPackage.setDataSource(dataSource);
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
    
    /**
     * Sửa: Thêm @Transactional và kiểm tra tồn tại trước khi xóa.
     */
    @Transactional
    public void deleteDataPackage(Long id) {
        // Kiểm tra gói dữ liệu có tồn tại không
        DataPackage dataPackage = dataPackageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Data package not found with id: " + id));
        
        // Xóa gói dữ liệu
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
    /**
 * Cập nhật thông tin của một gói dữ liệu.
 *
 * @param id ID của gói dữ liệu cần cập nhật.
 * @param request DTO chứa thông tin mới.
 * @return Optional chứa gói dữ liệu đã được cập nhật, hoặc Optional rỗng nếu không tìm thấy.
 */
@Transactional
public Optional<DataPackage> updateDataPackage(Long id, UpdateDataPackageRequest request) {
    return dataPackageRepository.findById(id).map(existingPackage -> {
        if (request.getName() != null) {
            existingPackage.setName(request.getName());
        }
        if (request.getDescription() != null) {
            existingPackage.setDescription(request.getDescription());
        }
        if (request.getDataType() != null) {
            existingPackage.setDataType(request.getDataType());
        }
        if (request.getFormat() != null) {
            existingPackage.setFormat(request.getFormat());
        }
        if (request.getPrice() != null) {
            existingPackage.setPrice(request.getPrice());
        }
        if (request.getPricingModel() != null) {
            existingPackage.setPricingModel(request.getPricingModel());
        }
        if (request.getStatus() != null) {
            existingPackage.setStatus(request.getStatus());
        }
        if (request.getSize() != null) {
            existingPackage.setSize(request.getSize());
        }
        return dataPackageRepository.save(existingPackage);
    });
}
}
