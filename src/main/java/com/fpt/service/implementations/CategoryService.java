package com.fpt.service.implementations;
import com.fpt.dto.BranchDTO;
import com.fpt.dto.CategoryDTO;
import com.fpt.entity.Branch;
import com.fpt.entity.Category;
import com.fpt.service.interfaces.ICategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class CategoryService implements ICategoryService {
    @Override
    public Page<CategoryDTO> getAllCategory(Pageable pageable, String search, Boolean isActive) {
        return null;
    }

    @Override
    public List<CategoryDTO> convertToDto(List<Category> data) {
        return List.of();
    }

    @Override
    public List<CategoryDTO> getAll() {
        return List.of();
    }

    @Override
    public CategoryDTO getById(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void deleteMore(List<Long> ids) {

    }
    private CategoryDTO toDto(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .isActive(category.getIsActive())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}
