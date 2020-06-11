package com.debrief2.pulsa.member.service.impl;

import com.debrief2.pulsa.member.model.OTP;
import com.debrief2.pulsa.member.service.OTPService;
import org.springframework.stereotype.Service;

@Service
public class OTPServiceImpl implements OTPService {

    @Override
    public void createOTP(long userId, String code) {

    }

    @Override
    public void updateOTP(long userId, String code) {

    }

    @Override
    public OTP sendOTP(long userId) {
        return null;
    }

    @Override
    public OTP getOTP(long userId) {
        return null;
    }

    @Override
    public OTP verifyOTP(long userId, String code) {
        return null;
    }
}
