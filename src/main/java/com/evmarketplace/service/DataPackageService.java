package com.evmarketplace.service;

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

    /**
     * Lấy tất cả data packages
     * @return List<DataPackage>
     */
    public List<DataPackage> findAll() {
        return dataPackageRepository.findAll();
    }

    /**
     * Tìm data package theo ID
     * @param id - ID của data package
     * @return Optional<DataPackage>
     */
    public Optional<DataPackage> findById(Long id) {
        return dataPackageRepository.findById(id);
    }
}