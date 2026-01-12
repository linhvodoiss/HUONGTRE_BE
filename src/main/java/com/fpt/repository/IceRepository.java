package com.fpt.repository;

import com.fpt.entity.Ice;
import com.fpt.entity.Topping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IceRepository extends JpaRepository<Ice, Long>, JpaSpecificationExecutor<Ice> {


}
