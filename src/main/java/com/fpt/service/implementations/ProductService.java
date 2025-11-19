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
//                .map(this::toDto);
        .map(product -> modelMapper.map(product, ProductDTO.class));
    }

    @Override
    public Page<ProductDTO> getAllProductCustomer( Pageable pageable, String search) {
        ProductSpecificationBuilder specification = new ProductSpecificationBuilder(search,true);
        return repository.findAll(specification.build(), pageable)
//                .map(this::toDto);
                .map(product -> modelMapper.map(product, ProductDTO.class));
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


//    public SubscriptionPackageDTO create(SubscriptionPackageDTO dto) {
//        return toDto(repository.save(toEntity(dto)));
//    }

//    @Override
//    public SubscriptionPackageDTO update(Long id, SubscriptionPackageDTO dto) {
//        SubscriptionPackage entity = repository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Subscription not found"));
//
//        entity.setName(dto.getName());
//        entity.setPrice(dto.getPrice());
//        entity.setDiscount(dto.getDiscount());
//        entity.setBillingCycle(SubscriptionPackage.BillingCycle.valueOf(dto.getBillingCycle()));
//        entity.setIsActive(dto.getIsActive());
//        entity.setSimulatedCount(dto.getSimulatedCount());
//        entity.setDescription(dto.getDescription());
//        if (dto.getOptionsId() != null && !dto.getOptionsId().isEmpty()) {
//            List<Option> options = optionRepository.findAllById(dto.getOptionsId());
//            if (options.size() != dto.getOptionsId().size()) {
//                throw new RuntimeException("Some Option IDs not found!");
//            }
//            entity.setOptions(options);
//        } else {
//            entity.setOptions(null);
//        }
//
//
//        return toDto(repository.save(entity));
//    }




    @Override
    public void delete(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        repository.delete(product);
    }


    @Override
    public void deleteMore(List<Long> ids) {
        List<Product> packages = repository.findAllById(ids);
        repository.deleteAll(packages);
    }


//    @Override
//    public SubscriptionPackageDTO create(SubscriptionPackageDTO dto) {
//        SubscriptionPackage entity = new SubscriptionPackage();
//        entity.setName(dto.getName());
//        entity.setPrice(dto.getPrice());
//        entity.setDiscount(dto.getDiscount());
//        entity.setBillingCycle(SubscriptionPackage.BillingCycle.valueOf(dto.getBillingCycle()));
//        entity.setTypePackage(SubscriptionPackage.TypePackage.valueOf(dto.getTypePackage()));
//        entity.setSimulatedCount(0L);
//        entity.setDescription(dto.getDescription());
//
//        if (dto.getOptionsId() != null && !dto.getOptionsId().isEmpty()) {
//            List<Option> options = optionRepository.findAllById(dto.getOptionsId());
//            if (options.size() != dto.getOptionsId().size()) {
//                throw new RuntimeException("One or more options not found");
//            }
//            entity.setOptions(options);
//        }
//
//
//        SubscriptionPackage saved = repository.save(entity);
//        return toDto(saved);
//    }


    private ProductDTO toDto(Product entity) {
        if (entity == null) {
            return null;
        }

        // Category object
        CategoryDTO categoryDto = null;
        if (entity.getCategory() != null) {
            categoryDto = CategoryDTO.builder()
                    .id(entity.getCategory().getId())
                    .name(entity.getCategory().getName())
                    .description(entity.getCategory().getDescription())
                    .imageUrl(entity.getCategory().getImageUrl())
                    .build();
        }

        // Toppings list
        List<ToppingDTO> toppingDTOs = null;
        if (entity.getToppings() != null) {
            toppingDTOs = entity.getToppings().stream()
                    .map(t -> ToppingDTO.builder()
                            .id(t.getId())
                            .name(t.getName())
                            .price(t.getPrice())
                            .build())
                    .toList();
        }

        // Sizes list
        List<SizeDTO> sizeDTOs = null;
        if (entity.getSizes() != null) {
            sizeDTOs = entity.getSizes().stream()
                    .map(s -> SizeDTO.builder()
                            .id(s.getId())
                            .name(s.getName())
                            .build())
                    .toList();
        }

        return ProductDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .imageUrl(entity.getImageUrl())
                .isActive(entity.getIsActive())
                .category(categoryDto)
                .toppings(toppingDTOs)
                .sizes(sizeDTOs)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

}
