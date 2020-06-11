package com.debrief2.pulsa.member.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OTPGenerator {

    final Random random = new Random();

    public String generateOtp() {
        return String.format("%04d", random.nextInt(10000));
    }

}