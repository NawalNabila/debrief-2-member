package com.debrief2.pulsa.member.service.impl;

import com.debrief2.pulsa.member.exception.ServiceException;
import com.debrief2.pulsa.member.model.OTP;
import com.debrief2.pulsa.member.payload.response.UserResponse;
import com.debrief2.pulsa.member.repository.OTPMapper;
import com.debrief2.pulsa.member.repository.UserMapper;
import com.debrief2.pulsa.member.service.OTPService;
import com.debrief2.pulsa.member.utils.OTPGenerator;
import com.debrief2.pulsa.member.utils.TwillioSMS;
import com.twilio.exception.ApiException;
import com.twilio.exception.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OTPServiceImpl implements OTPService {

    @Autowired
    private OTPGenerator generator;

    @Value("${phoneNumber}") private String myTwilioPhoneNumber;
    @Value("${twilioAccountSid}") private String twilioAccountSid;
    @Value("${twilioAuthToken}") private String twilioAuthToken;

    @Autowired
    private OTPMapper otpMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void createOTP(OTP otp) {
        otpMapper.createOTP(otp);
    }

    @Override
    public void updateOTP(OTP otp) {
        otpMapper.updateOTP(otp);
    }

    @Override
    public OTP sendOTP(long userId) throws ServiceException {
        UserResponse userResponse = userMapper.getUserById(userId);
        if (userResponse == null)
            throw new ServiceException("user not found");

        String phone = userResponse.getUsername();

        OTP otp = otpMapper.getOTP(userResponse.getId());
        if (otp != null) {
            try {
                //update OTP user
                otp.setCode(generator.generateOtp());
                TwillioSMS twilioSMS = new TwillioSMS();
                twilioSMS.send(myTwilioPhoneNumber, twilioAccountSid, twilioAuthToken, phone, otp.getCode());
                updateOTP(otp);
                throw new ServiceException("success");
            } catch (ApiException | AuthenticationException e) {
                //twilio free can only send to verified number
                throw new ServiceException("failed");
            }
        } else {
            try {
                //create OTP user
                OTP new_otp = new OTP();
                new_otp.setUserId(userResponse.getId());
                new_otp.setCode(generator.generateOtp());
                TwillioSMS twilioSMS = new TwillioSMS();
                twilioSMS.send(myTwilioPhoneNumber, twilioAccountSid, twilioAuthToken, phone, new_otp.getCode());
                createOTP(new_otp);
                throw new ServiceException("success");
            } catch (ApiException | AuthenticationException e) {
                //twilio free can only send to verified number
                throw new ServiceException("failed");
            }
        }
    }

    @Override
    public OTP getOTP(long userId) throws ServiceException {
        UserResponse user = userMapper.getUserById(userId);
        if (user == null)
            throw new ServiceException("user not found");

        return otpMapper.getOTP(user.getId());
    }

    @Override
    public OTP verifyOTP(long userId, String code) {
        return null;
    }
}
