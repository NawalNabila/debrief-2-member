package com.debrief2.pulsa.member.service.impl;

import com.debrief2.pulsa.member.exception.ServiceException;
import com.debrief2.pulsa.member.model.OTP;
import com.debrief2.pulsa.member.payload.response.UserResponse;
import com.debrief2.pulsa.member.repository.OTPMapper;
import com.debrief2.pulsa.member.repository.UserMapper;
import com.debrief2.pulsa.member.service.OTPService;
import com.debrief2.pulsa.member.utils.OTPGenerator;
import com.debrief2.pulsa.member.utils.TwillioSMS;
import com.debrief2.pulsa.member.utils.Validation;
import com.twilio.exception.ApiException;
import com.twilio.exception.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OTPServiceImpl implements OTPService {

    @Autowired
    private OTPGenerator generator;

    @Autowired
    private Validation validation;

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
                otp.setUpdatedAt(LocalDateTime.now().plusHours(7));
                otp.setCode(generator.generateOtp());
                TwillioSMS twilioSMS = new TwillioSMS();
                twilioSMS.send(myTwilioPhoneNumber, twilioAccountSid, twilioAuthToken, phone, otp.getCode());
                updateOTP(otp);
                return otp;
            } catch (ApiException | AuthenticationException e) {
                //twilio free can only send to verified number
                throw new ServiceException("unverified number");
            }
        } else {
            try {
                //create OTP user
                OTP new_otp = new OTP();
                new_otp.setUserId(userResponse.getId());
                new_otp.setCreatedAt(LocalDateTime.now().plusHours(7));
                new_otp.setUpdatedAt(LocalDateTime.now().plusHours(7));
                new_otp.setCode(generator.generateOtp());
                TwillioSMS twilioSMS = new TwillioSMS();
                twilioSMS.send(myTwilioPhoneNumber, twilioAccountSid, twilioAuthToken, phone, new_otp.getCode());
                createOTP(new_otp);
                return new_otp;
            } catch (ApiException | AuthenticationException e) {
                //twilio free can only send to verified number
                throw new ServiceException("unverified number");
            }
        }
    }

    @Override
    public OTP getOTP(long userId) throws ServiceException {
        UserResponse user = userMapper.getUserById(userId);
        if (user == null)
            throw new ServiceException("user not found");

        OTP otp = otpMapper.getOTP(user.getId());
        if (otp == null)
            throw new ServiceException("OTP not found");
        return otp;
    }

    @Override
    public OTP verifyOTP(long userId, String code) throws ServiceException {
        //validate OTP
        if (!validation.otp(code)){
            throw new ServiceException("invalid OTP");
        }

        OTP otp = otpMapper.getVerifyOTP(userId, code);
        if (otp == null)
            throw new ServiceException("incorrect OTP");

        //check whether already expired (lifetime of OTP code is 3m)
        if (otp.getUpdatedAt().plusMinutes(3).isBefore(LocalDateTime.now())) {
            throw new ServiceException("OTP expired");
        }

        return otp;
    }
}
