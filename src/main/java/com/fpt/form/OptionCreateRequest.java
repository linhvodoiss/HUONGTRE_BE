package com.fpt.form;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class OptionCreateRequest {
    private Long optionGroupId;
    private String name;
    private Double price;
    private Integer displayOrder;
    private Boolean isActive;
}
