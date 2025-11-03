package com.evmarketplace.repository;

import com.evmarketplace.entity.DataProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataProviderRepository extends JpaRepository<DataProvider, Long> {
    
    List<DataProvider> findByCompanyNameContainingIgnoreCase(String companyName);
    
    List<DataProvider> findByDataSourceType(DataProvider.DataSourceType dataSourceType);
    
    List<DataProvider> findByVerificationStatus(DataProvider.VerificationStatus verificationStatus);
}
