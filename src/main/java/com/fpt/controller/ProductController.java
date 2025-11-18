package com.fpt.controller;

import com.fpt.dto.ProductDTO;
import com.fpt.dto.SubscriptionPackageDTO;
import com.fpt.entity.SubscriptionPackage;
import com.fpt.payload.PaginatedResponse;
import com.fpt.payload.SuccessNoResponse;
import com.fpt.payload.SuccessResponse;
import com.fpt.service.interfaces.IProductService;
import com.fpt.service.interfaces.ISubscriptionPackageService;
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
@RequestMapping("/api/v1/products")
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
        Page<ProductDTO> dtoPage = service.getAllProduct(pageable, search,isActive);
        PaginatedResponse<ProductDTO> response = new PaginatedResponse<>(dtoPage, HttpServletResponse.SC_OK, "Lấy danh sách sản phẩm thành công");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/list")
    public ResponseEntity<PaginatedResponse<ProductDTO>> getAllProductsCustomer(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String search
    ) {
        Page<ProductDTO> dtoPage = service.getAllProductCustomer(pageable, search);
        PaginatedResponse<ProductDTO> response = new PaginatedResponse<>(dtoPage, HttpServletResponse.SC_OK, "Lấy danh sách sản phẩm thành công");
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<ProductDTO>> getById(@PathVariable Long id) {
        ProductDTO dto = service.getById(id);
        SuccessResponse<ProductDTO> response = new SuccessResponse<>(
                HttpServletResponse.SC_OK,
                "Lấy chi tiết sản phẩm thành công!",
                dto
        );
        return ResponseEntity.ok(response);
    }

//    @PostMapping
//    public ResponseEntity<SuccessResponse<SubscriptionPackageDTO>> create(@RequestBody SubscriptionPackageDTO dto) {
//        try {
//            SubscriptionPackageDTO saved = service.create(dto);
//            return ResponseEntity.ok(new SuccessResponse<>(200, "Create successfully!", saved));
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(new SuccessResponse<>(500, "Create failed!", null));
//        }
//    }
//
//
//    @PutMapping("/{id}")
//    public ResponseEntity<SuccessResponse<SubscriptionPackageDTO>> update(@PathVariable Long id, @RequestBody SubscriptionPackageDTO dto) {
//        try {
//            SubscriptionPackageDTO updated = service.update(id, dto);
//            return ResponseEntity.ok(new SuccessResponse<>(200, "Update successfully!", updated));
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(new SuccessResponse<>(500, "Update failed!", null));
//        }
//    }


    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessNoResponse> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok(new SuccessNoResponse(200, "Delete successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new SuccessNoResponse(500, "Delete failed!"));
        }
    }

    @DeleteMapping
    public ResponseEntity<SuccessNoResponse> deleteMore(@RequestBody List<Long> ids) {
        try {
            service.deleteMore(ids);
            return ResponseEntity.ok(new SuccessNoResponse(200, "Delete successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new SuccessNoResponse(500, "Delete failed!"));
        }
    }


}
