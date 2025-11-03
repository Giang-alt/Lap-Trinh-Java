package com.evmarketplace.repository;

import com.evmarketplace.entity.DataConsumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataConsumerRepository extends JpaRepository<DataConsumer, Long> {
    
    List<DataConsumer> findByCompanyNameContainingIgnoreCase(String companyName);
    
    List<DataConsumer> findByIndustry(String industry);
}
