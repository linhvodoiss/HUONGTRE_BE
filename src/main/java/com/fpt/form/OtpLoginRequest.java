package com.fpt.form;

import lombok.Data;

@Data
public class OtpLoginRequest {
    private String phoneNumber;
    private String otp;
}
