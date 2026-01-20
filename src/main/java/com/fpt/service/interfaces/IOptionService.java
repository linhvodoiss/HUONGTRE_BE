package com.fpt.service.interfaces;

import com.fpt.dto.OptionDTO;
import com.fpt.form.OptionCreateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOptionService {
    OptionDTO create(OptionCreateRequest req);
    OptionDTO update(Long id, OptionCreateRequest request);
    void delete(Long id);
    void deleteMany(List<Long> ids);
}
