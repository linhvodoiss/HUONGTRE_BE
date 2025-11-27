package com.fpt.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToppingDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;

    private Double price;
    private Boolean isAvailable;
    private Boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
