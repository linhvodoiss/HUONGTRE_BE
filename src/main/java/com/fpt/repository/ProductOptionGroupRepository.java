package com.fpt.repository;

import com.fpt.entity.Option;
import com.fpt.entity.ProductOptionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductOptionGroupRepository extends JpaRepository<ProductOptionGroup, Long>, JpaSpecificationExecutor<ProductOptionGroup> {


}
