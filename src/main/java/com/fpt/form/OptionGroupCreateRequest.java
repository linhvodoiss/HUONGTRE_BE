package com.fpt.form;

import com.fpt.enums.OptionSelectType;

import lombok.Data;
import lombok.NoArgsConstructor;

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
