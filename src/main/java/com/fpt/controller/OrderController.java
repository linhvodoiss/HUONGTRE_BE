package com.fpt.controller;

import com.fpt.annotation.CurrentUserId;
import com.fpt.dto.BranchDTO;
import com.fpt.dto.CategoryDTO;
import com.fpt.dto.OrderDTO;
import com.fpt.form.OrderCreateRequest;
import com.fpt.payload.PaginatedResponse;
import com.fpt.payload.SuccessNoResponse;
import com.fpt.payload.SuccessResponse;
import com.fpt.service.interfaces.IBranchService;
import com.fpt.service.interfaces.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {

    private final IOrderService service;

    @PostMapping
    public ResponseEntity<SuccessResponse<OrderDTO>> create(
            @RequestBody OrderCreateRequest request
    ) {
        OrderDTO dto = service.createOrder(request);

        return ResponseEntity.ok(
                new SuccessResponse<>(
                        HttpServletResponse.SC_OK,
                        "Bạn đã đặt hàng thành công!",
                        dto
                )
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<OrderDTO>> getById(@PathVariable Long id) {
        OrderDTO dto=service.getById(id);
        SuccessResponse<OrderDTO> response = new SuccessResponse<>(
                HttpServletResponse.SC_OK,
                "Lấy chi tiết đơn hàng " + id + " thành công!",
                dto
        );
        return ResponseEntity.ok(response);
    }



}
