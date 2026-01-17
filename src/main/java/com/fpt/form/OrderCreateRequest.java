package com.fpt.form;

import lombok.Data;

import java.util.List;
@Data
public class OrderCreateRequest {
    private String receiverName;
    private String receiverPhone;
    private String deliveryAddress;
    private String note;
    private List<OrderItemRequest> items;
}
