package com.fpt.form;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductCreateRequest {

    private String name;
    private String description;
    private Double price;
    private String imageUrl;

    private Long categoryId;
    private List<Long> optionGroupIds;

    private Boolean isActive = true;

}
