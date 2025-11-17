package com.fpt.repository;

import com.fpt.entity.BranchProduct;
import com.fpt.entity.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface BranchProductRepository extends JpaRepository<BranchProduct, Long>, JpaSpecificationExecutor<BranchProduct> {

}
