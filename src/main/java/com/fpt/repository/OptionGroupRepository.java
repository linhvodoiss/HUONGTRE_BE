package com.fpt.repository;

import com.fpt.entity.Option;
import com.fpt.entity.OptionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OptionGroupRepository extends JpaRepository<OptionGroup, Long>, JpaSpecificationExecutor<OptionGroup> {


}
