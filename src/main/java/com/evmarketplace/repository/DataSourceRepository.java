package com.evmarketplace.repository;

import com.evmarketplace.entity.DataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataSourceRepository extends JpaRepository<DataSource, Long> {
    
    List<DataSource> findByDataProviderId(Long dataProviderId);
    
    List<DataSource> findByType(DataSource.DataSourceType type);
    
    List<DataSource> findByStatus(DataSource.DataSourceStatus status);
    
    List<DataSource> findByNameContainingIgnoreCase(String name);
}
