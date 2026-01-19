package com.fpt.service.interfaces;

import com.fpt.dto.CustomerDTO;
import com.fpt.dto.OptionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICustomerService {
    Page<CustomerDTO> getAllCustomer(Pageable pageable, String search);
    List<CustomerDTO> getAll();
    CustomerDTO getById(Long id);
}
