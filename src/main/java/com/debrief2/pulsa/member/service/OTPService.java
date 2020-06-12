package com.debrief2.pulsa.member.service;

import com.debrief2.pulsa.member.exception.ServiceException;
import com.debrief2.pulsa.member.model.OTP;

public interface OTPService {
    void createOTP(OTP otp);
    void updateOTP(OTP otp);
    OTP sendOTP(long userId) throws ServiceException;
    OTP getOTP(long userId) throws ServiceException;
    OTP verifyOTP(long userId, String code) throws ServiceException;
}
