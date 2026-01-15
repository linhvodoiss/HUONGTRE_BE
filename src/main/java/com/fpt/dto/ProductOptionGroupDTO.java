package com.fpt.dto;

import com.fpt.enums.OptionSelectType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionGroupDTO {
    private Long id;

    private Long productId;
    private Long optionGroupId;

    private OptionGroupDTO optionGroup;
}
