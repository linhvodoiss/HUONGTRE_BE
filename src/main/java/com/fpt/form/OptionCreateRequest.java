package com.fpt.form;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OptionCreateRequest {
    private Long optionGroupId;
    private String name;
    private Double price;
    private Integer displayOrder;
    private Boolean isActive;
}
