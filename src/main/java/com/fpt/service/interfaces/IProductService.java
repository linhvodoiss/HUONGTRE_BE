package com.fpt.service.interfaces;

import com.fpt.dto.ProductDTO;
import com.fpt.dto.SubscriptionPackageDTO;
import com.fpt.entity.Product;
import com.fpt.entity.SubscriptionPackage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductService {
    Page<ProductDTO> getAllProduct(Pageable pageable, String search, Boolean isActive);
    Page<ProductDTO> getAllProductCustomer(Pageable pageable, String search);
    List<ProductDTO> convertToDto(List<Product> data);
    List<ProductDTO> getAll();
    ProductDTO getById(Long id);
//    ProductDTO create(ProductDTO dto);
//    ProductDTO update(Long id, ProductDTO dto);
    void delete(Long id);
    void deleteMore(List<Long> ids);
}
