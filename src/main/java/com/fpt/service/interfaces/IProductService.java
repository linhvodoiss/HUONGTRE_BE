package com.fpt.service.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fpt.dto.ProductDTO;
import com.fpt.entity.Product;
import com.fpt.form.ProductCreateRequest;

public interface IProductService {
    Page<ProductDTO> getAllProduct(Pageable pageable, String search, Boolean isActive);

    Page<ProductDTO> getAllProductCustomer(Pageable pageable, String search);

    List<ProductDTO> convertToDto(List<Product> data);

    List<ProductDTO> getAll();

    ProductDTO getById(Long id);

    ProductDTO create(ProductCreateRequest request);

    // ProductDTO create(ProductDTO dto);
    // ProductDTO update(Long id, ProductDTO dto);
    void delete(Long id);

    void deleteMore(List<Long> ids);
}
