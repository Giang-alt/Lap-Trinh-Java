package com.evmarketplace.service;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.evmarketplace.entity.DataPackage;
import com.evmarketplace.entity.DataSource;
import com.evmarketplace.enums.DataType;
import com.evmarketplace.enums.DataFormat;
import com.evmarketplace.enums.PackageStatus;
import com.evmarketplace.enums.PricingModel;
import com.evmarketplace.repository.DataPackageRepository;
import com.evmarketplace.repository.DataSourceRepository;
import com.evmarketplace.request.CreateDataPackageRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
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

private final DataSourceRepository dataSourceRepository;

@Transactional
public DataPackage createDataPackage(CreateDataPackageRequest request) {
    DataSource dataSource = dataSourceRepository.findById(request.getDataSourceId())
            .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy nguồn dữ liệu"));

    DataPackage dataPackage = new DataPackage();
    dataPackage.setName(request.getName());
    dataPackage.setDescription(request.getDescription());
    dataPackage.setDataType(request.getDataType());
    dataPackage.setFormat(request.getFormat());
    dataPackage.setPrice(request.getPrice());
    dataPackage.setPricingModel(request.getPricingModel());
    dataPackage.setStatus(request.getStatus());
    dataPackage.setDataSource(dataSource);
    dataPackage.setSizeInMb(request.getSizeInMb());
    dataPackage.setCreatedDate(LocalDateTime.now());
    return dataPackageRepository.save(dataPackage);
}
