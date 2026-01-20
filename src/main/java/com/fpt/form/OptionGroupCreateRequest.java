package com.fpt.form;

import com.fpt.enums.OptionSelectType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class OptionGroupCreateRequest {

    private String name;
    private OptionSelectType selectType;
    private Boolean required;
    private Integer minSelect;
    private Integer maxSelect;
    private Integer displayOrder;
    private Boolean isActive;
}
