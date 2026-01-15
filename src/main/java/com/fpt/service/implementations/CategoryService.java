package com.fpt.service.implementations;
import com.fpt.dto.*;
import com.fpt.entity.*;
import com.fpt.repository.CategoryRepository;
import com.fpt.repository.OptionRepository;
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

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService implements ICategoryService {
    private final CategoryRepository repository;
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;
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

    @Override
    @Transactional(readOnly = true)
    public List<CategoryMenuDTO> getFullMenu() {

        List<Category> categories =
                repository.findByIsActiveTrueOrderByIdAsc();

        return categories.stream()
                .map(this::mapCategoryToMenuDTO)
                .toList();
    }
    ////////////////////////////////Response menu///////////////////////////////
    private CategoryMenuDTO mapCategoryToMenuDTO(Category category) {

        List<Product> products =
                productRepository.findMenuProductsByCategory(category.getId());

        List<ProductDetailDTO> productDTOs = products.stream()
                .map(this::mapProductToDetailDTO)
                .toList();

        return CategoryMenuDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .products(productDTOs)
                .build();
    }



    private ProductDetailDTO mapProductToDetailDTO(Product product) {

        List<ProductOptionGroup> pogs = product.getProductOptionGroups();

        List<OptionGroup> optionGroups = pogs.stream()
                .map(ProductOptionGroup::getOptionGroup)
                .toList();

        List<Long> optionGroupIds = optionGroups.stream()
                .map(OptionGroup::getId)
                .toList();

        List<Option> options = optionRepository.findActiveByOptionGroupIds(optionGroupIds);

        Map<Long, List<OptionDTO>> optionMap =
                options.stream()
                        .collect(Collectors.groupingBy(
                                o -> o.getOptionGroup().getId(),
                                Collectors.mapping(this::toOptionDTO, Collectors.toList())
                        ));

        List<OptionGroupDTO> ogDTOs = optionGroups.stream()
                .map(og -> OptionGroupDTO.builder()
                        .id(og.getId())
                        .name(og.getName())
                        .selectType(og.getSelectType())
                        .required(og.getRequired())
                        .minSelect(og.getMinSelect())
                        .maxSelect(og.getMaxSelect())
                        .displayOrder(og.getDisplayOrder())
                        .options(optionMap.getOrDefault(og.getId(), List.of()))
                        .build())
                .toList();

        return ProductDetailDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .description(product.getDescription())
                .optionGroups(ogDTOs)
                .build();
    }

    private OptionDTO toOptionDTO(Option option) {
        return OptionDTO.builder()
                .id(option.getId())
                .name(option.getName())
                .description(option.getDescription())
                .price(option.getPrice())
                .build();
    }


////////////////////////////////Response base category///////////////////////////////
    private CategoryDTO toDto(Category category) {
        return modelMapper.map(category, CategoryDTO.class);
    }


}

