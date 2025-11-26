package com.fpt.service.interfaces;

import com.fpt.dto.CategoryDTO;
import com.fpt.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoryService {
    Page<CategoryDTO> getAllCategory(Pageable pageable, String search, Boolean isActive);
    List<CategoryDTO> convertToDto(List<Category> data);
    List<CategoryDTO> getAll();
    CategoryDTO getById(Long id);
    void delete(Long id);
    void deleteMore(List<Long> ids);
}
