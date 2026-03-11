package com.fpt.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fpt.dto.OrderDTO;
import com.fpt.form.OrderCreateRequest;

public interface IOrderService {
    Page<OrderDTO> getAllOrders(Pageable pageable, String search);

    OrderDTO createOrder(OrderCreateRequest request);

    OrderDTO getById(Long orderId);
}
