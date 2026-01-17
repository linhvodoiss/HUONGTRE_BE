package com.fpt.service.interfaces;

import com.fpt.dto.BranchDTO;
import com.fpt.dto.OrderDTO;
import com.fpt.entity.Branch;
import com.fpt.form.OrderCreateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService {
    OrderDTO createOrder(OrderCreateRequest request);

    OrderDTO getById(Long orderId);
}
