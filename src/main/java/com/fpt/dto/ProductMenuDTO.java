package com.fpt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductMenuDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;

    private List<OptionGroupDTO> optionGroups;


}
