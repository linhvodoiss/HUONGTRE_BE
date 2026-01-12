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
        List<Product> products = repository.findAllById(ids);
        repository.deleteAll(products);
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


        // Sizes through ProductSize
        List<ProductSizeDTO> sizeDTOs = null;
        if (entity.getProductSizes() != null) {
            sizeDTOs = entity.getProductSizes().stream()
                    .map(ps -> ProductSizeDTO.builder()
                            .sizeId(ps.getSize().getId())
                            .sizeName(ps.getSize().getName())
                            .price(ps.getPrice())
                            .build())
                    .toList();
        }
        // Toppings
        List<ToppingDTO> toppingDTOs = null;
        if (entity.getToppings() != null) {
            toppingDTOs = entity.getToppings().stream()
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

        // Ices
        List<IceDTO> iceDTOs = null;
        if (entity.getIces() != null) {
            iceDTOs = entity.getIces().stream()
                    .map(topping -> IceDTO.builder()
                            .id(topping.getId())
                            .name(topping.getName())
                            .description(topping.getDescription())
                            .imageUrl(topping.getImageUrl())
                            .isAvailable(topping.getIsActive())
                            .isActive(topping.getIsActive())
                            .createdAt(topping.getCreatedAt())
                            .updatedAt(topping.getUpdatedAt())
                            .build())
                    .toList();
        }

        // Sugars
        List<SugarDTO> sugarDTOS = null;
        if (entity.getSugars() != null) {
            sugarDTOS = entity.getSugars().stream()
                    .map(topping -> SugarDTO.builder()
                            .id(topping.getId())
                            .name(topping.getName())
                            .description(topping.getDescription())
                            .imageUrl(topping.getImageUrl())
                            .isAvailable(topping.getIsActive())
                            .isActive(topping.getIsActive())
                            .createdAt(topping.getCreatedAt())
                            .updatedAt(topping.getUpdatedAt())
                            .build())
                    .toList();
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
                .sizes(sizeDTOs)
                .toppings(toppingDTOs)
                .sugars(sugarDTOS)
                .ices(iceDTOs)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }


}
