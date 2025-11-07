package com.evmarketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.evmarketplace.entity.DataPackage;

/**
 * Repository cho Data Package
 *
 * Kế thừa JpaRepository để có sẵn các method:
 * - findAll() - Lấy tất cả data packages (dùng cho API của Giang)
 * - findById() - Tìm theo ID
 * - save() - Lưu/cập nhật
 * - deleteById() - Xóa theo ID
 */
@Repository
public interface DataPackageRepository extends JpaRepository<DataPackage, Long> {
    // JpaRepository đã cung cấp sẵn method findAll() cho API của Giang
}
