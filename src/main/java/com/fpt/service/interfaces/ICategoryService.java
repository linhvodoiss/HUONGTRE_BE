package com.fpt.service.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fpt.dto.CategoryDTO;
import com.fpt.dto.CategoryMenuDTO;
import com.fpt.entity.Category;

public interface ICategoryService {
    Page<CategoryDTO> getAllCategory(Pageable pageable, String search, Boolean isActive);

    Page<CategoryDTO> getAllCategoryCustomer(Pageable pageable, String search);

    List<CategoryDTO> convertToDto(List<Category> data);

    List<CategoryDTO> getAll();

    List<CategoryMenuDTO> getFullMenu();

    CategoryDTO getById(Long id);

    CategoryDTO create(CategoryDTO dto);

    CategoryDTO update(Long id, CategoryDTO dto);

    void delete(Long id);

    void deleteMore(List<Long> ids);
}
