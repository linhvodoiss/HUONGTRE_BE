package com.fpt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // Category
    private Long categoryId;
    private String categoryName;
    private CategoryDTO category;
    private List<ProductSizeDTO> sizes;
    private List<ToppingDTO> toppings;
    private List<IceDTO> ices;
    private List<SugarDTO> sugars;
}
