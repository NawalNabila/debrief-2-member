package com.debrief2.pulsa.member.utils;

import com.debrief2.pulsa.member.exception.ServiceException;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class Validation {
    final String emailRegex = ".+@.+\\..+";
    final String pinRegex = "^[1-9]{1}[0-9]{5}$";

    public boolean validateEmail (String email) throws ServiceException {
        if (!Pattern.matches(emailRegex, email))
            throw new ServiceException("invalid email");
        return true;
    }

    public boolean validatePin (String pin) throws ServiceException {
        if (!Pattern.matches(pinRegex, pin))
            throw new ServiceException("invalid pin");
        return true;
    }

    public boolean validateOtp (String otp) throws ServiceException {
        if (otp.length()!=4){
            throw new ServiceException("invalid OTP");
        }
        try {
            Long.parseLong(otp);
            return true;
        } catch (Exception e) {
            throw new ServiceException("invalid OTP");
        }
    }

    public boolean validatePhone(String phone) throws ServiceException {
        if (phone.length()<10||phone.length()>14){
            throw new ServiceException("invalid phone number");
        }
        try {
            Long.parseLong(phone);
            return true;
        } catch (Exception e) {
            throw new ServiceException("invalid phone number");
        }
    }

    public String convertPhone(String phone) {
        if (phone.charAt(0)=='+'){
            return phone.substring(1);
        }
        if (phone.charAt(0)=='0'){
            return "62" + phone.substring(1);
        }
        return phone;
    }

    public boolean validateUser(String emailUser, String phoneUser, String pinUser) throws ServiceException {
        if (validateEmail(emailUser)) {
            if (validatePhone(phoneUser)) {
                validatePin(pinUser);
            }
        }
        return true;
    }
}
