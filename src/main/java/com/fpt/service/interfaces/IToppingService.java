package com.fpt.service.interfaces;

import com.fpt.dto.CategoryDTO;
import com.fpt.dto.ProductDTO;
import com.fpt.dto.ToppingDTO;
import com.fpt.entity.Product;
import com.fpt.entity.Topping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IToppingService {
    Page<ToppingDTO> getAllTopping(Pageable pageable, String search, Boolean isActive);
    Page<ToppingDTO> getAllToppingCustomer(Pageable pageable, String search);
    List<ToppingDTO> convertToDto(List<Topping> data);
    List<ToppingDTO> getAll();
    ToppingDTO getById(Long id);
    void delete(Long id);
    void deleteMore(List<Long> ids);
}
