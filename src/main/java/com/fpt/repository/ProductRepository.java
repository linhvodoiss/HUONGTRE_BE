package com.fpt.repository;

import com.fpt.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @Query("""
    SELECT DISTINCT p
    FROM Product p
    JOIN FETCH p.productOptionGroups pog
    JOIN FETCH pog.optionGroup og
    WHERE p.category.id = :categoryId
      AND p.isActive = true
""")
    List<Product> findMenuProductsByCategory(@Param("categoryId") Long categoryId);

}
