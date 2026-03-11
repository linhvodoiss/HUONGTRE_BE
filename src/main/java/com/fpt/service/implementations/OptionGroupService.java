package com.fpt.service.implementations;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fpt.dto.OptionDTO;
import com.fpt.dto.OptionGroupDTO;
import com.fpt.entity.Option;
import com.fpt.entity.OptionGroup;
import com.fpt.form.OptionGroupCreateRequest;
import com.fpt.repository.OptionGroupRepository;
import com.fpt.service.interfaces.IOptionGroupService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OptionGroupService implements IOptionGroupService {

    private final OptionGroupRepository repository;

    @Override
    @Transactional(readOnly = true)
    public OptionGroupDTO getDetail(Long id) {
        OptionGroup group = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("OptionGroup not found"));

        return toDto(group);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OptionGroupDTO> getList() {
        return repository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OptionGroupDTO create(OptionGroupCreateRequest request) {

        OptionGroup group = repository.save(
                OptionGroup.builder()
                        .name(request.getName())
                        .selectType(request.getSelectType())
                        .required(request.getRequired())
                        .minSelect(request.getMinSelect())
                        .maxSelect(request.getMaxSelect())
                        .displayOrder(request.getDisplayOrder())
                        .isActive(true)
                        .build());

        return toDto(group);

    }

    @Override
    @Transactional
    public OptionGroupDTO update(Long id, OptionGroupCreateRequest request) {

        OptionGroup group = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("OptionGroup not found"));

        group.setName(request.getName());
        group.setSelectType(request.getSelectType());
        group.setRequired(request.getRequired());
        group.setMinSelect(request.getMinSelect());
        group.setMaxSelect(request.getMaxSelect());
        group.setDisplayOrder(request.getDisplayOrder());
        group.setIsActive(request.getIsActive());
        return toDto(group);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Option not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public void deleteMany(List<Long> ids) {
        List<OptionGroup> options = repository.findAllById(ids);
        if (options.size() != ids.size()) {
            throw new RuntimeException("One or more Option IDs not found!");
        }
        repository.deleteAll(options);
    }

    private OptionDTO toOptionDto(Option option) {
        return OptionDTO.builder()
                .id(option.getId())
                .name(option.getName())
                .price(option.getPrice())
                .displayOrder(option.getDisplayOrder())
                .isActive(option.getIsActive())
                .createdAt(option.getCreatedAt())
                .updatedAt(option.getUpdatedAt())
                .build();
    }

    private OptionGroupDTO toDto(OptionGroup optionGroup) {
        return OptionGroupDTO.builder()
                .id(optionGroup.getId())
                .name(optionGroup.getName())
                .selectType(optionGroup.getSelectType())
                .required(optionGroup.getRequired())
                .minSelect(optionGroup.getMinSelect())
                .maxSelect(optionGroup.getMaxSelect())
                .displayOrder(optionGroup.getDisplayOrder())
                .isActive(optionGroup.getIsActive())
                .createdAt(optionGroup.getCreatedAt())
                .updatedAt(optionGroup.getUpdatedAt())
                .options(
                        optionGroup.getOptions() == null
                                ? List.of()
                                : optionGroup.getOptions()
                                        .stream()
                                        .map(this::toOptionDto)
                                        .collect(Collectors.toList()))
                .build();
    }
}
