package com.fpt.service.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fpt.dto.BranchDTO;
import com.fpt.entity.Branch;

public interface IBranchService {
    Page<BranchDTO> getAllBranch(Pageable pageable, String search, Boolean isActive);

    Page<BranchDTO> getAllBranchCustomer(Pageable pageable, String search);

    List<BranchDTO> convertToDto(List<Branch> data);

    List<BranchDTO> getAll();

    BranchDTO getById(Long id);

    void delete(Long id);

    void deleteMore(List<Long> ids);
}
