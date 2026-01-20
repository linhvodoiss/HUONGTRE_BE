package com.fpt.service.interfaces;

import com.fpt.dto.OptionDTO;
import com.fpt.dto.OptionGroupDTO;
import com.fpt.form.OptionGroupCreateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOptionGroupService {
    OptionGroupDTO getDetail(Long id);

    List<OptionGroupDTO> getList();
    OptionGroupDTO create(OptionGroupCreateRequest request);
    OptionGroupDTO update(Long id, OptionGroupCreateRequest request);
    void delete(Long id);
    void deleteMany(List<Long> ids);
}
