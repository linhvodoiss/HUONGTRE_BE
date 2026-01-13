package com.fpt.service.implementations;
import com.fpt.dto.*;
import com.fpt.entity.*;
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
import java.util.stream.Collectors;

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
                .map(this::toDtoSuper)
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

    private CategoryDTO toDtoSuper(Category category) {

        List<ProductDTO> productDTOS = null;

        if (category.getProducts() != null) {
            productDTOS = category.getProducts().stream()
                    .map(product -> {

                        // ===== Sizes =====
                        List<ProductSizeDTO> sizeDTOs = null;
                        if (product.getProductSizes() != null) {
                            sizeDTOs = product.getProductSizes().stream()
                                    .map(ps -> ProductSizeDTO.builder()
                                            .sizeId(ps.getSize().getId())
                                            .sizeName(ps.getSize().getName())
                                            .price(ps.getPrice())
                                            .build())
                                    .toList();
                        }

                        // ===== Toppings =====
                        List<ToppingDTO> toppingDTOs = null;
                        if (product.getToppings() != null) {
                            toppingDTOs = product.getToppings().stream()
                                    .map(topping -> ToppingDTO.builder()
                                            .id(topping.getId())
                                            .name(topping.getName())
                                            .description(topping.getDescription())
                                            .price(topping.getPrice())
                                            .imageUrl(topping.getImageUrl())
                                            .isAvailable(topping.getIsActive())
                                            .isActive(topping.getIsActive())
                                            .createdAt(topping.getCreatedAt())
                                            .updatedAt(topping.getUpdatedAt())
                                            .build())
                                    .toList();
                        }

                        // ===== Ices =====
                        List<IceDTO> iceDTOs = null;
                        if (product.getIces() != null) {
                            iceDTOs = product.getIces().stream()
                                    .map(ice -> IceDTO.builder()
                                            .id(ice.getId())
                                            .name(ice.getName())
                                            .description(ice.getDescription())
                                            .imageUrl(ice.getImageUrl())
                                            .isAvailable(ice.getIsActive())
                                            .isActive(ice.getIsActive())
                                            .createdAt(ice.getCreatedAt())
                                            .updatedAt(ice.getUpdatedAt())
                                            .build())
                                    .toList();
                        }

                        // ===== Sugars =====
                        List<SugarDTO> sugarDTOs = null;
                        if (product.getSugars() != null) {
                            sugarDTOs = product.getSugars().stream()
                                    .map(sugar -> SugarDTO.builder()
                                            .id(sugar.getId())
                                            .name(sugar.getName())
                                            .description(sugar.getDescription())
                                            .imageUrl(sugar.getImageUrl())
                                            .isAvailable(sugar.getIsActive())
                                            .isActive(sugar.getIsActive())
                                            .createdAt(sugar.getCreatedAt())
                                            .updatedAt(sugar.getUpdatedAt())
                                            .build())
                                    .toList();
                        }

                        return ProductDTO.builder()
                                .id(product.getId())
                                .name(product.getName())
                                .description(product.getDescription())
                                .imageUrl(product.getImageUrl())
                                .isActive(product.getIsActive())
                                .sizes(sizeDTOs)
                                .toppings(toppingDTOs)
                                .ices(iceDTOs)
                                .sugars(sugarDTOs)
                                .createdAt(product.getCreatedAt())
                                .updatedAt(product.getUpdatedAt())
                                .build();
                    })
                    .toList();
        }

        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .isActive(category.getIsActive())
                .products(productDTOS)
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }


}

