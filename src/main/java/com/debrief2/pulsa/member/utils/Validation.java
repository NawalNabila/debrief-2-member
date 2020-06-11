package com.debrief2.pulsa.member.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class Validation {
    final String emailRegex = ".+@.+\\..+";
    final String pinRegex = "^[1-9]{1}[0-9]{5}$";

    public boolean email (String email) {
        return Pattern.matches(emailRegex, email);
    }

    public boolean pin (String pin) {
        return Pattern.matches(pinRegex, pin);
    }

    public boolean otp (String otp) {
        if (otp.length()!=4){
            return false;
        }
        try {
            Long.parseLong(otp);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean phone(String phone) {
        if (phone.length()<9||phone.length()>14){
            return false;
        }
        try {
            Long.parseLong(phone);
            return true;
        } catch (Exception e) {
            return false;
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
}
