package com.debrief2.pulsa.member.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class OTPTest {

    private static final long ID = 1;
    private static final long USER_ID = 2;
    private static final String CODE = "1234";
    private static final LocalDateTime CREATED_AT = LocalDateTime.now().plusHours(7);
    private static final LocalDateTime UPDATED_AT = LocalDateTime.now().plusHours(7);
    private static final LocalDateTime NEW_TIME = LocalDateTime.now();

    @Test
    public void constructorTest() {
        OTP otp = new OTP(ID, USER_ID, CODE, CREATED_AT, UPDATED_AT);
        assert otp.getCode().equals(CODE);
        assert otp.getCreatedAt().equals(CREATED_AT);
        assert otp.getUpdatedAt().equals(UPDATED_AT);
    }

    @Test
    public void constructorEmptyTest() {
        new OTP();
    }

    @Test
    public void setterId() {
        OTP otp = new OTP(ID, USER_ID, CODE, CREATED_AT, UPDATED_AT);
        otp.setId(2);
        otp.getId();
    }

    @Test
    public void setterUserID() {
        OTP otp = new OTP(ID, USER_ID, CODE, CREATED_AT, UPDATED_AT);
        otp.setUserId(2);
        otp.getUserId();
    }

    @Test
    public void setterCode() {
        OTP otp = new OTP(ID, USER_ID, CODE, CREATED_AT, UPDATED_AT);
        otp.setCode("2255");
        assert otp.getCode().equals("2255");
    }

    @Test
    public void setterCreatedAt() {
        OTP otp = new OTP(ID, USER_ID, CODE, CREATED_AT, UPDATED_AT);

        otp.setCreatedAt(NEW_TIME);
        assert otp.getCreatedAt().equals(NEW_TIME);
    }

    @Test
    public void setterUpdatedAt() {
        OTP otp = new OTP(ID, USER_ID, CODE, CREATED_AT, UPDATED_AT);

        otp.setUpdatedAt(NEW_TIME);
        assert otp.getUpdatedAt().equals(NEW_TIME);
    }
}