package com.fpt.repository;

import com.fpt.entity.Branch;
import com.fpt.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    long countByCustomerId(Long customerId);

    List<Order> findByCustomerPhone(String phone);

}
