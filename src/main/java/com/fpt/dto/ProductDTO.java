package com.fpt.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Category
    private Long categoryId;
    private String categoryName;

    // BranchProducts
    private List<BranchProductDTO> branchProducts;

    private CategoryDTO category;
    private List<ToppingDTO> toppings;
    private List<ProductSizeDTO> sizes;

}
