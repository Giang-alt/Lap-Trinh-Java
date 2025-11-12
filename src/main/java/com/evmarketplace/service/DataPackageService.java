package com.evmarketplace.service;

import java.math.BigDecimal; // Đã thêm
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page; // Đã thêm
import org.springframework.data.domain.Pageable; // Đã thêm
import org.springframework.stereotype.Service;

import com.evmarketplace.entity.DataPackage;
import com.evmarketplace.repository.DataPackageRepository;
// Bạn cần chắc chắn các Enum này đã được import
import com.evmarketplace.enums.DataType;
import com.evmarketplace.enums.DataFormat;
import com.evmarketplace.enums.PackageStatus;
import com.evmarketplace.enums.PricingModel;
@Service
public class DataPackageService {

    @Autowired
    private DataPackageRepository dataPackageRepository;

    // ... (Phương thức findAll() và findById() nếu có)

    /**
     * Tìm kiếm gói dữ liệu theo nhiều tiêu chí
     * @param name - Tên gói dữ liệu (tìm kiếm gần đúng)
     * @param dataType - Loại dữ liệu
     * @param format - Định dạng
     * @param minPrice - Giá tối thiểu
     * @param maxPrice - Giá tối đa
     * @param status - Trạng thái
     * @param pricingModel - Mô hình giá
     * @param pageable - Thông tin phân trang và sắp xếp
     * @return Page<DataPackage>
     */
    public Page<DataPackage> searchDataPackages(
            String name,
            DataType dataType,
            DataFormat format,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            PackageStatus status,
            PricingModel pricingModel,
            Pageable pageable) {
        
        return dataPackageRepository.searchDataPackages(
                name, dataType, format, minPrice, maxPrice, status, pricingModel, pageable
        );
    }
}