package com.debrief2.pulsa.member.service;

import com.debrief2.pulsa.member.exception.ServiceException;
import com.debrief2.pulsa.member.model.OTP;
import com.debrief2.pulsa.member.payload.response.OTPResponse;

public interface OTPService {
    void createOTP(OTP otp);
    void updateOTP(OTP otp);
    OTPResponse sendOTP(long userId) throws ServiceException;
    OTPResponse getOTP(long userId) throws ServiceException;
    OTPResponse verifyOTP(long userId, String code) throws ServiceException;
}
