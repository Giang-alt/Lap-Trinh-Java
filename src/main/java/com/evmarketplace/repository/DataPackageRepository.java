package com.evmarketplace.repository;

import com.evmarketplace.entity.DataPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DataPackageRepository extends JpaRepository<DataPackage, Long> {
    
    List<DataPackage> findByDataSourceId(Long dataSourceId);
    
    List<DataPackage> findByDataType(DataPackage.DataType dataType);
    
    List<DataPackage> findByFormat(DataPackage.DataFormat format);
    
    List<DataPackage> findByStatus(DataPackage.PackageStatus status);
    
    List<DataPackage> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT dp FROM DataPackage dp WHERE dp.price BETWEEN :minPrice AND :maxPrice")
    List<DataPackage> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);
    
    @Query("SELECT dp FROM DataPackage dp WHERE dp.dataSource.dataProvider.id = :dataProviderId")
    List<DataPackage> findByDataProviderId(@Param("dataProviderId") Long dataProviderId);
}
