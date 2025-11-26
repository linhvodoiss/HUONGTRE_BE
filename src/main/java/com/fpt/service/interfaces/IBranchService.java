package com.fpt.service.interfaces;

import com.fpt.dto.BranchDTO;
import com.fpt.entity.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IBranchService {
    Page<BranchDTO> getAllBranch(Pageable pageable, String search, Boolean isActive);
    List<BranchDTO> convertToDto(List<Branch> data);
    List<BranchDTO> getAll();
    BranchDTO getById(Long id);
    void delete(Long id);
    void deleteMore(List<Long> ids);
}
