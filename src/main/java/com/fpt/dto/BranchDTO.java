package com.fpt.dto;

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
public class BranchDTO {
	private Long id;
	private String name;
	private String description;
	private String imageUrl;
	private String address;
	private String phone;
	private Boolean isActive;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private List<BranchProductDTO> branchProducts;
}
