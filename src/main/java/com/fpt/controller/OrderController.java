package com.fpt.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpt.constant.ApiPaths;
import com.fpt.constant.ResponseMessage;
import com.fpt.dto.OrderDTO;
import com.fpt.form.OrderCreateRequest;
import com.fpt.payload.PaginatedResponse;
import com.fpt.payload.SuccessResponse;
import com.fpt.service.interfaces.IOrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(ApiPaths.ORDERS)
@RequiredArgsConstructor
@Validated
public class OrderController {

    private final IOrderService service;

    @GetMapping
    public ResponseEntity<PaginatedResponse<OrderDTO>> getAll(
            Pageable pageable,
            @RequestParam(required = false) String search) {
        Page<OrderDTO> dtoPage = service.getAllOrders(pageable, search);
        PaginatedResponse<OrderDTO> response = new PaginatedResponse<>(dtoPage, HttpServletResponse.SC_OK,
                ResponseMessage.GET_LIST_SUBSCRIPTION_SUCCESS);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<OrderDTO>> create(
            @RequestBody OrderCreateRequest request) {
        OrderDTO dto = service.createOrder(request);

        return ResponseEntity.ok(
                new SuccessResponse<>(
                        HttpServletResponse.SC_OK,
                        ResponseMessage.CREATE_ORDER_SUCCESS,
                        dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<OrderDTO>> getById(@PathVariable Long id) {
        OrderDTO dto = service.getById(id);
        SuccessResponse<OrderDTO> response = new SuccessResponse<>(
                HttpServletResponse.SC_OK,
                ResponseMessage.GET_ORDER_DETAIL_SUCCESS + id + " thành công!",
                dto);
        return ResponseEntity.ok(response);
    }

}
