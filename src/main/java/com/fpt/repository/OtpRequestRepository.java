package com.fpt.repository;

import com.fpt.entity.OtpRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface OtpRequestRepository extends JpaRepository<OtpRequest, Long>, JpaSpecificationExecutor<OtpRequest> {
    Optional<OtpRequest> findTopByPhoneNumberAndOtpCodeAndIsUsedFalseOrderByCreatedAtDesc(
            String phoneNumber, String otpCode
    );
}
