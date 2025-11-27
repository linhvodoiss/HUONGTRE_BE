package com.fpt.service.implementations;

import com.fpt.dto.BranchDTO;
import com.fpt.dto.CategoryDTO;
import com.fpt.dto.ToppingDTO;
import com.fpt.entity.Category;
import com.fpt.entity.Topping;
import com.fpt.repository.ProductRepository;
import com.fpt.repository.ToppingRepository;
import com.fpt.service.interfaces.IToppingService;
import com.fpt.specification.CategorySpecificationBuilder;
import com.fpt.specification.ToppingSpecificationBuilder;
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
public class ToppingService implements IToppingService {

    private final ToppingRepository repository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public Page<ToppingDTO> getAllTopping(Pageable pageable, String search, Boolean isActive) {
        ToppingSpecificationBuilder specification = new ToppingSpecificationBuilder(search,isActive);
        return repository.findAll(specification.build(), pageable)
                .map(this::toDto);
    }

    @Override
    public Page<ToppingDTO> getAllToppingCustomer(Pageable pageable, String search) {
        ToppingSpecificationBuilder specification = new ToppingSpecificationBuilder(search,true);
        return repository.findAll(specification.build(), pageable)
                .map(this::toDto);
    }

    @Override
    public List<ToppingDTO> convertToDto(List<Topping> data) {
        return List.of();
    }

    @Override
    public List<ToppingDTO> getAll() {
        return repository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public ToppingDTO getById(Long id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Topping not found"));
    }

    @Override
    public void delete(Long id) {
        Topping topping = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Topping not found"));
        repository.delete(topping);
    }

    @Override
    public void deleteMore(List<Long> ids) {
        List<Topping> toppings = repository.findAllById(ids);
        repository.deleteAll(toppings);
    }
    private ToppingDTO toDto(Topping topping) {
        return modelMapper.map(topping, ToppingDTO.class);
    }
}
