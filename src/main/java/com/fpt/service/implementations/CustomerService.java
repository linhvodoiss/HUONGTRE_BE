package com.fpt.service.implementations;

import com.fpt.dto.CustomerDTO;
import com.fpt.dto.OptionDTO;
import com.fpt.entity.Customer;
import com.fpt.entity.Option;
import com.fpt.repository.CustomerRepository;
import com.fpt.repository.OptionRepository;
import com.fpt.service.interfaces.ICustomerService;
import com.fpt.service.interfaces.IOptionService;
import com.fpt.specification.CustomerSpecificationBuilder;
import com.fpt.specification.OptionSpecificationBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;
    @Override
    public Page<CustomerDTO> getAllCustomer(Pageable pageable, String search) {
        CustomerSpecificationBuilder specification = new CustomerSpecificationBuilder(search);
        return customerRepository.findAll(specification.build(), pageable)
                .map(this::toDto);
    }
    @Override
    public List<CustomerDTO> getAll() {
        return customerRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return toDto(customer);
    }


    private CustomerDTO toDto(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .phone(customer.getPhone())
                .note(customer.getNote())
                .totalOrders(customer.getTotalOrders())
                .totalSpent(customer.getTotalSpent())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }
}
