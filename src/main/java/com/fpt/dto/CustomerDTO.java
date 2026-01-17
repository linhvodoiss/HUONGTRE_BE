package com.fpt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fpt.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDTO {
    private Long id;
    private String phone;
    private String note;
    private Integer totalOrders;
    private Double totalSpent;
}
