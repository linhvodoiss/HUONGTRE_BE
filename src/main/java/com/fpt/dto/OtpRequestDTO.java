package com.fpt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpRequestDTO {
    private Long id;
    private String phoneNumber;
    private String otpCode;
    private LocalDateTime expiredAt;
    private LocalDateTime createdAt;
    private Boolean isUsed;
}
