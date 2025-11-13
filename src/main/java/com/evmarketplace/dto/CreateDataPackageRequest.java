package com.evmarketplace.dto;

import com.evmarketplace.enums.DataFormat;
import com.evmarketplace.enums.DataType;
import com.evmarketplace.enums.PackageStatus;
import com.evmarketplace.enums.PricingModel;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class CreateDataPackageRequest {

    @NotBlank(message = "Tên gói dữ liệu không được bỏ trống")
    private String name;

    @Size(max = 500, message = "Mô tả tối đa 500 ký tự")
    private String description;

    @NotNull
    private DataType dataType;

    @NotNull
    private DataFormat format;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá phải lớn hơn 0")
    private BigDecimal price;

    @NotNull
    private PricingModel pricingModel;

    @NotNull
    private PackageStatus status;

    @NotNull
    private Long dataSourceId;

    @Positive(message = "Kích thước phải lớn hơn 0")
    private Long sizeInMb;

    // Getters & setters
}