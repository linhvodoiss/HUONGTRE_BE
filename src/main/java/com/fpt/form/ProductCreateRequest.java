package com.fpt.form;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

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
