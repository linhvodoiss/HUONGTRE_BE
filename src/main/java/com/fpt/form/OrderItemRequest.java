package com.fpt.form;

import lombok.Data;
import java.util.List;

@Data
public class OrderItemRequest {
    private Long productId;
    private Integer quantity;
    private String note;
    private List<Long> optionIds;
}
