package com.fpt.service.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fpt.dto.CustomerDTO;

public interface ICustomerService {
    Page<CustomerDTO> getAllCustomer(Pageable pageable, String search);

    List<CustomerDTO> getAll();

    CustomerDTO getById(Long id);
}
