package com.fpt.service.implementations;

import com.fpt.dto.CategoryDTO;
import com.fpt.dto.ToppingDTO;
import com.fpt.entity.Category;
import com.fpt.entity.Topping;
import com.fpt.service.interfaces.IToppingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class ToppingService implements IToppingService {
    @Override
    public Page<ToppingDTO> getAllTopping(Pageable pageable, String search, Boolean isActive) {
        return null;
    }

    @Override
    public List<ToppingDTO> convertToDto(List<Topping> data) {
        return List.of();
    }

    @Override
    public List<ToppingDTO> getAll() {
        return List.of();
    }

    @Override
    public ToppingDTO getById(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void deleteMore(List<Long> ids) {

    }
    private ToppingDTO toDto(Topping topping) {
        return ToppingDTO.builder()
                .id(topping.getId())
                .name(topping.getName())
                .price(topping.getPrice())
                .description(topping.getDescription())
                .imageUrl(topping.getImageUrl())
                .isActive(topping.getIsActive())
                .isAvailable(topping.getIsAvailable())
                .createdAt(topping.getCreatedAt())
                .updatedAt(topping.getUpdatedAt())
                .build();
    }
}
