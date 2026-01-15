package com.fpt.service.implementations;

import com.fpt.dto.*;
import com.fpt.entity.Option;
import com.fpt.entity.PaymentOrder;
import com.fpt.entity.Product;
import com.fpt.entity.SubscriptionPackage;
import com.fpt.repository.OptionRepository;
import com.fpt.repository.PaymentOrderRepository;
import com.fpt.repository.ProductRepository;
import com.fpt.repository.SubscriptionPackageRepository;
import com.fpt.service.interfaces.IProductService;
import com.fpt.service.interfaces.ISubscriptionPackageService;
import com.fpt.specification.ProductSpecificationBuilder;
import com.fpt.specification.SubscriptionPackageSpecificationBuilder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService implements IProductService {

    private final ProductRepository repository;

    @Autowired
private ModelMapper modelMapper;
    @Override
    public Page<ProductDTO> getAllProduct(Pageable pageable, String search, Boolean isActive) {
        ProductSpecificationBuilder specification = new ProductSpecificationBuilder(search,isActive);
        return repository.findAll(specification.build(), pageable)
                .map(this::toDto);
    }

    @Override
    public Page<ProductDTO> getAllProductCustomer( Pageable pageable, String search) {
        ProductSpecificationBuilder specification = new ProductSpecificationBuilder(search,true);
        return repository.findAll(specification.build(), pageable)
                .map(this::toDto);
    }

    @Override
    public List<ProductDTO> convertToDto(List<Product> products) {
        List<ProductDTO> subscriptionPackageDTOs = new ArrayList<>();
        for (Product product : products) {
            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
            subscriptionPackageDTOs.add(productDTO);
        }
        return subscriptionPackageDTOs;
    }

    @Override
    public List<ProductDTO> getAll() {
        return repository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public ProductDTO getById(Long id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }


    @Override
    public void delete(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        repository.delete(product);
    }


    @Override
    public void deleteMore(List<Long> ids) {
        List<Product> products = repository.findAllById(ids);
        repository.deleteAll(products);
    }



    private ProductDTO toDto(Product entity) {
        if (entity == null) {
            return null;
        }

        // Category
        CategoryDTO categoryDto = null;
        if (entity.getCategory() != null) {
            categoryDto = CategoryDTO.builder()
                    .id(entity.getCategory().getId())
                    .name(entity.getCategory().getName())
                    .description(entity.getDescription())
                    .imageUrl(entity.getImageUrl())
                    .isActive(entity.getIsActive())
                    .createdAt(entity.getCreatedAt())
                    .updatedAt(entity.getUpdatedAt())
                    .build();
        }


        return ProductDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .imageUrl(entity.getImageUrl())
                .isActive(entity.getIsActive())
                .categoryId(entity.getCategory().getId())
                .categoryName(entity.getCategory().getName())
                .category(categoryDto)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }


}
