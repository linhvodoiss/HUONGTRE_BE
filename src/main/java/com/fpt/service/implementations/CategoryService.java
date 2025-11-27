package com.fpt.service.implementations;
import com.fpt.dto.BranchDTO;
import com.fpt.dto.CategoryDTO;
import com.fpt.dto.ProductDTO;
import com.fpt.dto.ToppingDTO;
import com.fpt.entity.Branch;
import com.fpt.entity.Category;
import com.fpt.entity.Product;
import com.fpt.entity.Topping;
import com.fpt.repository.CategoryRepository;
import com.fpt.repository.ProductRepository;
import com.fpt.service.interfaces.ICategoryService;
import com.fpt.specification.CategorySpecificationBuilder;
import com.fpt.specification.ProductSpecificationBuilder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService implements ICategoryService {
    private final CategoryRepository repository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public Page<CategoryDTO> getAllCategory(Pageable pageable, String search, Boolean isActive) {
        CategorySpecificationBuilder specification = new CategorySpecificationBuilder(search,isActive);
        return repository.findAll(specification.build(), pageable)
                .map(this::toDto);
    }
    @Override
    public Page<CategoryDTO> getAllCategoryCustomer( Pageable pageable, String search) {
        CategorySpecificationBuilder specification = new CategorySpecificationBuilder(search,true);
        return repository.findAll(specification.build(), pageable)
                .map(this::toDto);
    }
    @Override
    public List<CategoryDTO> convertToDto(List<Category> data) {
        return List.of();
    }

    @Override
    public List<CategoryDTO> getAll() {
        return repository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public CategoryDTO getById(Long id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public CategoryDTO create(CategoryDTO dto) {
        Category category = modelMapper.map(dto, Category.class);

        if (category.getIsActive() == null) {
            category.setIsActive(true);
        }
        category.setId(null);
        Category savedCategory = repository.save(category);
        return toDto(savedCategory);
    }

    @Override
    public CategoryDTO update(Long id, CategoryDTO dto) {
        Category existingCategory = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        modelMapper.map(dto, existingCategory);
        existingCategory.setId(id);
        Category updatedCategory = repository.save(existingCategory);
        return toDto(updatedCategory);
    }

    @Override
    public void delete(Long id) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        repository.delete(category);
    }


    @Override
    public void deleteMore(List<Long> ids) {
        List<Category> categories = repository.findAllById(ids);
        repository.deleteAll(categories);
    }

    private CategoryDTO toDto(Category category) {
            return modelMapper.map(category, CategoryDTO.class);
    }
}
