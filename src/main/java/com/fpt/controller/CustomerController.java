package com.fpt.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpt.dto.CustomerDTO;
import com.fpt.payload.PaginatedResponse;
import com.fpt.payload.SuccessResponse;
import com.fpt.service.interfaces.ICustomerService;

import lombok.RequiredArgsConstructor;

import com.fpt.constant.ApiPaths;
import com.fpt.constant.ResponseMessage;

@RestController
@RequestMapping(ApiPaths.CUSTOMERS)
@RequiredArgsConstructor
@Validated
public class CustomerController {

    private final ICustomerService customerService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<CustomerDTO>> getAll(
            Pageable pageable,
            @RequestParam(required = false) String search) {
        Page<CustomerDTO> dtoPage = customerService.getAllCustomer(pageable, search);
        PaginatedResponse<CustomerDTO> response = new PaginatedResponse<>(dtoPage, HttpServletResponse.SC_OK,
                ResponseMessage.GET_LIST_CUSTOMER_SUCCESS);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<SuccessResponse<List<CustomerDTO>>> getListAll() {
        List<CustomerDTO> list = customerService.getAll();
        return ResponseEntity.ok(new SuccessResponse<>(200, ResponseMessage.GET_LIST_CUSTOMER_SUCCESS_2, list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<CustomerDTO>> getById(@PathVariable Long id) {
        CustomerDTO dto = customerService.getById(id);
        return ResponseEntity.ok(new SuccessResponse<>(200, ResponseMessage.GET_CUSTOMER_DETAIL_SUCCESS, dto));
    }

}
