package com.fpt.service.implementations;

import com.fpt.dto.BranchDTO;
import com.fpt.entity.Branch;
import com.fpt.repository.BranchRepository;
import com.fpt.service.interfaces.IBranchService;
import com.fpt.specification.BranchSpecificationBuilder;
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
public class BranchService implements IBranchService {

    private final BranchRepository repository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public Page<BranchDTO> getAllBranch(Pageable pageable, String search, Boolean isActive) {
        BranchSpecificationBuilder specification = new BranchSpecificationBuilder(search,isActive);
        return repository.findAll(specification.build(), pageable)
                .map(this::toDto);
    }

    @Override
    public Page<BranchDTO> getAllBranchCustomer(Pageable pageable, String search) {
        BranchSpecificationBuilder specification = new BranchSpecificationBuilder(search,true);
        return repository.findAll(specification.build(), pageable)
                .map(this::toDto);
    }

    @Override
    public List<BranchDTO> convertToDto(List<Branch> data) {
        return List.of();
    }

    @Override
    public List<BranchDTO> getAll() {
        return repository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public BranchDTO getById(Long id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Branch not found"));
    }

    @Override
    public void delete(Long id) {
        Branch branch = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found"));
        repository.delete(branch);
    }

    @Override
    public void deleteMore(List<Long> ids) {
        List<Branch> branches = repository.findAllById(ids);
        repository.deleteAll(branches);
    }
    private BranchDTO toDto(Branch branch) {
        return modelMapper.map(branch, BranchDTO.class);
    }
}
