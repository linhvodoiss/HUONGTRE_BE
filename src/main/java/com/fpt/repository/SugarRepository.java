package com.fpt.repository;

import com.fpt.entity.Sugar;
import com.fpt.entity.Topping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SugarRepository extends JpaRepository<Sugar, Long>, JpaSpecificationExecutor<Sugar> {


}
