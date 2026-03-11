package com.fpt.service.implementations;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.fpt.dto.OptionDTO;
import com.fpt.entity.Option;
import com.fpt.entity.OptionGroup;
import com.fpt.form.OptionCreateRequest;
import com.fpt.repository.OptionGroupRepository;
import com.fpt.repository.OptionRepository;
import com.fpt.service.interfaces.IOptionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OptionService implements IOptionService {

    private final OptionRepository optionRepository;
    private final OptionGroupRepository optionGroupRepository;

    @Override
    @Transactional
    public OptionDTO create(OptionCreateRequest req) {

        OptionGroup group = optionGroupRepository.findById(req.getOptionGroupId())
                .orElseThrow(() -> new RuntimeException("OptionGroup not found"));

        Option option = optionRepository.save(
                Option.builder()
                        .optionGroup(group)
                        .name(req.getName())
                        .price(req.getPrice())
                        .displayOrder(req.getDisplayOrder())
                        .isActive(true)
                        .isDeleted(false)
                        .build());
        return toDto(option);
    }

    @Override
    @Transactional
    public OptionDTO update(Long id, OptionCreateRequest request) {

        Option option = optionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Option not found"));

        option.setName(request.getName());
        option.setPrice(request.getPrice());
        option.setDisplayOrder(request.getDisplayOrder());
        option.setIsActive(request.getIsActive());

        return toDto(option);
    }

    @Override
    public void delete(Long id) {
        if (!optionRepository.existsById(id)) {
            throw new RuntimeException("Option not found with id: " + id);
        }
        optionRepository.deleteById(id);
    }

    @Override
    public void deleteMany(List<Long> ids) {
        List<Option> options = optionRepository.findAllById(ids);
        if (options.size() != ids.size()) {
            throw new RuntimeException("One or more Option IDs not found!");
        }
        optionRepository.deleteAll(options);
    }

    private OptionDTO toDto(Option option) {
        return OptionDTO.builder()
                .id(option.getId())
                .name(option.getName())
                .price(option.getPrice())
                .isActive(option.getIsActive())
                .displayOrder(option.getDisplayOrder())
                .createdAt(option.getCreatedAt())
                .updatedAt(option.getUpdatedAt())
                .build();
    }
}
