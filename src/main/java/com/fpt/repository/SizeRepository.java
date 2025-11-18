package com.fpt.repository;

import com.fpt.entity.Size;
import com.fpt.entity.Topping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SizeRepository extends JpaRepository<Size, Long>, JpaSpecificationExecutor<Size> {


}
