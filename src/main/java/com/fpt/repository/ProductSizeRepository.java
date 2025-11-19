package com.fpt.repository;

import com.fpt.entity.Product;
import com.fpt.entity.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductSizeRepository extends JpaRepository<ProductSize, Long>, JpaSpecificationExecutor<ProductSize> {


}
