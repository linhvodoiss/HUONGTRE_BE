package com.fpt.service.interfaces;

import java.util.List;

import com.fpt.dto.OptionDTO;
import com.fpt.form.OptionCreateRequest;

public interface IOptionService {
    OptionDTO create(OptionCreateRequest req);

    OptionDTO update(Long id, OptionCreateRequest request);

    void delete(Long id);

    void deleteMany(List<Long> ids);
}
