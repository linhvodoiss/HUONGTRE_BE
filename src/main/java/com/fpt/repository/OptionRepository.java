package com.fpt.repository;

import com.fpt.entity.License;
import com.fpt.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Long>, JpaSpecificationExecutor<Option> {
    @Query("""
    SELECT o
    FROM Option o
    WHERE o.optionGroup.id IN :groupIds
      AND o.isActive = true
""")
    List<Option> findActiveByOptionGroupIds(@Param("groupIds") List<Long> groupIds);


}
