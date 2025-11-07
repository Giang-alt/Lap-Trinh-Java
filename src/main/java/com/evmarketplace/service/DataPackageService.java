package com.evmarketplace.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evmarketplace.entity.DataPackage;
import com.evmarketplace.repository.DataPackageRepository;

/**
 * Service xử lý business logic cho Data Package
 *
 * Methods của Giang:
 * - findAll() - Lấy tất cả data packages
 */
@Service
public class DataPackageService {

    @Autowired
    private DataPackageRepository dataPackageRepository;

    /**
     * Lấy tất cả data packages
     * @return Danh sách tất cả DataPackage
     */
    public List<DataPackage> findAll() {
        return dataPackageRepository.findAll();
    }
}
