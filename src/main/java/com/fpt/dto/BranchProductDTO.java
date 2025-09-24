package com.fpt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchProductDTO {
	private Long id;

	private Double price;
	private Boolean isAvailable;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	private Long branchId;
	private String branchName;

	private Long productId;
	private String productName;
}
