package com.debrief2.pulsa.member.service;

import com.debrief2.pulsa.member.model.OTP;

public interface OTPService {
    void createOTP(long userId, String code);
    void updateOTP(long userId, String code);
    OTP sendOTP(long userId);
    OTP getOTP(long userId);
    OTP verifyOTP(long userId, String code);
}
