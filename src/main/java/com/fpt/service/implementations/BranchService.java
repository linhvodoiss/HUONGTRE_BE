package com.fpt.service.implementations;

import com.fpt.dto.BranchDTO;
import com.fpt.dto.OptionDTO;
import com.fpt.entity.Branch;
import com.fpt.entity.Option;
import com.fpt.repository.BranchRepository;
import com.fpt.repository.ProductRepository;
import com.fpt.service.interfaces.IBranchService;
import lombok.RequiredArgsConstructor;
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
    @Override
    public Page<BranchDTO> getAllBranch(Pageable pageable, String search, Boolean isActive) {
        return null;
    }

    @Override
    public List<BranchDTO> convertToDto(List<Branch> data) {
        return List.of();
    }

    @Override
    public List<BranchDTO> getAll() {
        return List.of();
    }

    @Override
    public BranchDTO getById(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void deleteMore(List<Long> ids) {

    }
    private BranchDTO toDto(Branch branch) {
        return BranchDTO.builder()
                .id(branch.getId())
                .name(branch.getName())
                .description(branch.getDescription())
                .imageUrl(branch.getImageUrl())
                .address(branch.getAddress())
                .phone(branch.getPhone())
                .isActive(branch.getIsActive())
                .createdAt(branch.getCreatedAt())
                .updatedAt(branch.getUpdatedAt())
                .build();
    }
}
