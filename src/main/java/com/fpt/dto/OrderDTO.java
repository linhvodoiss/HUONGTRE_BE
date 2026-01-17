package com.fpt.dto;

import com.fpt.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private Integer totalAmount;
    private String status;
    private LocalDateTime createdAt;
    private CustomerDTO customer;
    private String receiverName;
    private String receiverPhone;
    private String deliveryAddress;
    private String note;
    private List<OrderItemDTO> items;
}
