package com.fpt.service.interfaces;

import java.util.List;

import com.fpt.dto.OptionGroupDTO;
import com.fpt.form.OptionGroupCreateRequest;

public interface IOptionGroupService {
    OptionGroupDTO getDetail(Long id);

    List<OptionGroupDTO> getList();

    OptionGroupDTO create(OptionGroupCreateRequest request);

    OptionGroupDTO update(Long id, OptionGroupCreateRequest request);

    void delete(Long id);

    void deleteMany(List<Long> ids);
}
