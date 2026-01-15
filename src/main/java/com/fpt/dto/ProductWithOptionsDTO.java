package com.fpt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductWithOptionsDTO {
    private Long id;
    private String name;
    private Double price;

    private String imageUrl;
    private String description;

    private List<OptionGroupDTO> optionGroups;
}
