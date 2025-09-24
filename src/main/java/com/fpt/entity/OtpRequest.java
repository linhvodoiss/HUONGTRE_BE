package com.fpt.entity;

import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name = "OtpRequest")
public class OtpRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String otpCode;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    private Boolean isUsed = false;
}
