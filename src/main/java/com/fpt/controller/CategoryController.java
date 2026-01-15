package com.fpt.controller;

import com.fpt.dto.CategoryDTO;
import com.fpt.dto.CategoryMenuDTO;
import com.fpt.dto.OptionDTO;
import com.fpt.dto.ProductDTO;
import com.fpt.payload.PaginatedResponse;
import com.fpt.payload.SuccessNoResponse;
import com.fpt.payload.SuccessResponse;
import com.fpt.service.interfaces.ICategoryService;
import com.fpt.service.interfaces.IProductService;
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
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final ICategoryService service;

    @GetMapping
    public ResponseEntity<SuccessResponse<List<CategoryDTO>>> getAll() {
        List<CategoryDTO> dto = service.getAll();

        SuccessResponse<List<CategoryDTO>> response = new SuccessResponse<>(
                HttpServletResponse.SC_OK,
                "Lấy danh mục thành công!",
                dto
        );

        return ResponseEntity.ok(response);
    }
    @GetMapping("/menu")
    public ResponseEntity<SuccessResponse<List<CategoryMenuDTO>>> getFullMenu() {
        List<CategoryMenuDTO> dto = service.getFullMenu();
        SuccessResponse<List<CategoryMenuDTO>> response = new SuccessResponse<>(
                HttpServletResponse.SC_OK,
                "Lấy menu thành công!",
                dto
        );

        return ResponseEntity.ok(response);
    }


    @GetMapping("/list")
    public ResponseEntity<PaginatedResponse<CategoryDTO>> getAllCategories(
             Pageable pageable,
            @RequestParam(required = false) String search,
             @RequestParam(required = false) Boolean isActive

    ) {
        Page<CategoryDTO> dtoPage = service.getAllCategory(pageable, search,isActive);
        PaginatedResponse<CategoryDTO> response = new PaginatedResponse<>(dtoPage, HttpServletResponse.SC_OK, "Lấy danh sách danh mục thành công");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/list")
    public ResponseEntity<PaginatedResponse<CategoryDTO>> getAllCategoriesCustomer(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String search
    ) {
        Page<CategoryDTO> dtoPage = service.getAllCategoryCustomer(pageable, search);
        PaginatedResponse<CategoryDTO> response = new PaginatedResponse<>(dtoPage, HttpServletResponse.SC_OK, "Lấy danh sách danh mục thành công");
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<CategoryDTO>> getById(@PathVariable Long id) {
        CategoryDTO dto = service.getById(id);
        SuccessResponse<CategoryDTO> response = new SuccessResponse<>(
                HttpServletResponse.SC_OK,
                "Lấy chi tiết danh mục thành công!",
                dto
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<CategoryDTO>> create(@RequestBody CategoryDTO dto) {
        CategoryDTO created = service.create(dto);
        return ResponseEntity.ok(new SuccessResponse<>(200, "Create successfully!", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<CategoryDTO>> update(@PathVariable Long id, @RequestBody CategoryDTO dto) {
        CategoryDTO updated = service.update(id, dto);
        return ResponseEntity.ok(new SuccessResponse<>(200, "Update successfully!", updated));
    }



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
