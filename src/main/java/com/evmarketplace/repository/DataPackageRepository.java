package com.evmarketplace.repository;

import com.evmarketplace.entity.DataPackage;
import com.evmarketplace.enums.DataType;
import com.evmarketplace.enums.DataFormat;
import com.evmarketplace.enums.PackageStatus;
import com.evmarketplace.enums.PricingModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface DataPackageRepository extends JpaRepository<DataPackage, Long> {
    
    // ... (Phương thức searchDataPackages đã có)
    @Query("SELECT dp FROM DataPackage dp WHERE " +
           "(:name IS NULL OR LOWER(dp.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:dataType IS NULL OR dp.dataType = :dataType) AND " +
           "(:format IS NULL OR dp.format = :format) AND " +
           "(:minPrice IS NULL OR dp.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR dp.price <= :maxPrice) AND " +
           "(:status IS NULL OR dp.status = :status) AND " +
           "(:pricingModel IS NULL OR dp.pricingModel = :pricingModel)")
    Page<DataPackage> searchDataPackages(
            @Param("name") String name,
            @Param("dataType") DataType dataType,
            @Param("format") DataFormat format,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("status") PackageStatus status,
            @Param("pricingModel") PricingModel pricingModel,
            Pageable pageable
    );
}