package com.fpt.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpt.dto.ProductDTO;
import com.fpt.form.ProductCreateRequest;
import com.fpt.payload.PaginatedResponse;
import com.fpt.payload.SuccessNoResponse;
import com.fpt.payload.SuccessResponse;
import com.fpt.service.interfaces.IProductService;

import lombok.RequiredArgsConstructor;

import com.fpt.constant.ApiPaths;
import com.fpt.constant.ResponseMessage;

@RestController
@RequestMapping(ApiPaths.PRODUCTS)
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final IProductService service;

    @GetMapping
    public List<ProductDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/list")
    public ResponseEntity<PaginatedResponse<ProductDTO>> getAllProducts(
            Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean isActive

    ) {
        Page<ProductDTO> dtoPage = service.getAllProduct(pageable, search, isActive);
        PaginatedResponse<ProductDTO> response = new PaginatedResponse<>(dtoPage, HttpServletResponse.SC_OK,
                "Lấy danh sách sản phẩm thành công");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/list")
    public ResponseEntity<PaginatedResponse<ProductDTO>> getAllProductsCustomer(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String search) {
        Page<ProductDTO> dtoPage = service.getAllProductCustomer(pageable, search);
        PaginatedResponse<ProductDTO> response = new PaginatedResponse<>(dtoPage, HttpServletResponse.SC_OK,
                "Lấy danh sách sản phẩm thành công");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<ProductDTO>> getById(@PathVariable Long id) {
        ProductDTO dto = service.getById(id);
        SuccessResponse<ProductDTO> response = new SuccessResponse<>(
                HttpServletResponse.SC_OK,
                ResponseMessage.GET_PRODUCT_DETAIL_SUCCESS,
                dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<ProductDTO>> createProduct(
            @RequestBody ProductCreateRequest request) {
        ProductDTO dto = service.create(request);
        SuccessResponse<ProductDTO> response = new SuccessResponse<>(
                HttpServletResponse.SC_OK,
                ResponseMessage.CREATE_PRODUCT_SUCCESS,
                dto);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessNoResponse> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok(new SuccessNoResponse(200, ResponseMessage.DELETE_SUCCESS));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new SuccessNoResponse(500, ResponseMessage.DELETE_FAILED));
        }
    }

    @DeleteMapping
    public ResponseEntity<SuccessNoResponse> deleteMore(@RequestBody List<Long> ids) {
        try {
            service.deleteMore(ids);
            return ResponseEntity.ok(new SuccessNoResponse(200, ResponseMessage.DELETE_SUCCESS));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new SuccessNoResponse(500, ResponseMessage.DELETE_FAILED));
        }
    }

}
