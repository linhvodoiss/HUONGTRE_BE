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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpt.dto.CategoryDTO;
import com.fpt.dto.CategoryMenuDTO;
import com.fpt.payload.PaginatedResponse;
import com.fpt.payload.SuccessNoResponse;
import com.fpt.payload.SuccessResponse;
import com.fpt.service.interfaces.ICategoryService;

import lombok.RequiredArgsConstructor;

import com.fpt.constant.ApiPaths;
import com.fpt.constant.ResponseMessage;

@RestController
@RequestMapping(ApiPaths.CATEGORIES)
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final ICategoryService service;

    @GetMapping
    public ResponseEntity<SuccessResponse<List<CategoryDTO>>> getAll() {
        List<CategoryDTO> dto = service.getAll();

        SuccessResponse<List<CategoryDTO>> response = new SuccessResponse<>(
                HttpServletResponse.SC_OK,
                ResponseMessage.GET_CATEGORY_SUCCESS,
                dto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/menu")
    public ResponseEntity<SuccessResponse<List<CategoryMenuDTO>>> getFullMenu() {
        List<CategoryMenuDTO> dto = service.getFullMenu();
        SuccessResponse<List<CategoryMenuDTO>> response = new SuccessResponse<>(
                HttpServletResponse.SC_OK,
                ResponseMessage.GET_MENU_SUCCESS,
                dto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<PaginatedResponse<CategoryDTO>> getAllCategories(
            Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean isActive

    ) {
        Page<CategoryDTO> dtoPage = service.getAllCategory(pageable, search, isActive);
        PaginatedResponse<CategoryDTO> response = new PaginatedResponse<>(dtoPage, HttpServletResponse.SC_OK,
                ResponseMessage.GET_LIST_CATEGORY_SUCCESS);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/list")
    public ResponseEntity<PaginatedResponse<CategoryDTO>> getAllCategoriesCustomer(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String search) {
        Page<CategoryDTO> dtoPage = service.getAllCategoryCustomer(pageable, search);
        PaginatedResponse<CategoryDTO> response = new PaginatedResponse<>(dtoPage, HttpServletResponse.SC_OK,
                ResponseMessage.GET_LIST_CATEGORY_SUCCESS);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<CategoryDTO>> getById(@PathVariable Long id) {
        CategoryDTO dto = service.getById(id);
        SuccessResponse<CategoryDTO> response = new SuccessResponse<>(
                HttpServletResponse.SC_OK,
                ResponseMessage.GET_CATEGORY_DETAIL_SUCCESS,
                dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<CategoryDTO>> create(@RequestBody CategoryDTO dto) {
        CategoryDTO created = service.create(dto);
        return ResponseEntity.ok(new SuccessResponse<>(200, ResponseMessage.CREATE_SUCCESS, created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<CategoryDTO>> update(@PathVariable Long id, @RequestBody CategoryDTO dto) {
        CategoryDTO updated = service.update(id, dto);
        return ResponseEntity.ok(new SuccessResponse<>(200, ResponseMessage.UPDATE_SUCCESS, updated));
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
