package com.debrief2.pulsa.member.service;

import com.debrief2.pulsa.member.exception.ServiceException;
import com.debrief2.pulsa.member.model.OTP;
import com.debrief2.pulsa.member.payload.response.OTPResponse;
import com.debrief2.pulsa.member.payload.response.UserResponse;
import com.debrief2.pulsa.member.repository.OTPMapper;
import com.debrief2.pulsa.member.repository.UserMapper;
import com.debrief2.pulsa.member.service.impl.OTPServiceImpl;
import com.debrief2.pulsa.member.utils.OTPGenerator;
import com.debrief2.pulsa.member.utils.TwillioSMS;
import com.debrief2.pulsa.member.utils.Validation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class OTPServiceTest {

    @Mock
    private OTPMapper otpMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private Validation validation;

    @Mock
    private OTPGenerator generator;

    @Mock
    private TwillioSMS twillioSMS;

    @InjectMocks
    private OTPServiceImpl otpService;

    private static final long ID = 1;
    private static final long OTP_ID = 1;
    private static final String CODE = "4367";
    private static final String NAME = "Abigail";
    private static final String EMAIL = "abigail@dana.id";
    private static final String USERNAME = "6282272068810";
    private static final String TWILLIO_NUMBER = "+12057073799";
    private static final String TWILLIO_SID = "ACff298cada0f691c04c79f321c838ec90";
    private static final String TWILLIO_TOKEN = "96cafe8d6166cee6ae5b315b5a999b22";

    @Test
    public void sendOTPIdNullTest() {
        try {
            otpService.sendOTP(0);
        } catch (NullPointerException | ServiceException e) {
            assert e.getMessage().equals("id should not be empty");
        }
    }

    @Test
    public void sendOTPUserNotFoundTest() {
        try {
            when(userMapper.getUserById(ID)).thenReturn(null);
            otpService.sendOTP(ID);
        } catch (ServiceException e) {
            assert e.getMessage().equals("user not found");
        }
    }

    @Test
    public void createOTPTest() {
        OTP otp = new OTP();
        otp.setUserId(ID);
        otp.setCode(CODE);
        otp.setCreatedAt(LocalDateTime.now().plusHours(7));
        otp.setUpdatedAt(LocalDateTime.now().plusHours(7));

        doNothing().when(otpMapper).createOTP(otp);
        otpService.createOTP(otp);

        verify(otpMapper, times(1)).createOTP(otp);
    }

    @Test
    public void updateOTPTest() {
        OTP otp = new OTP();
        otp.setUserId(ID);
        otp.setCode(CODE);
        otp.setCreatedAt(LocalDateTime.now().plusHours(7));
        otp.setUpdatedAt(LocalDateTime.now().plusHours(7));

        doNothing().when(otpMapper).updateOTP(otp);
        otpService.updateOTP(otp);

        verify(otpMapper, times(1)).updateOTP(otp);
    }

//    @Test
//    public void OTPSendSuccessTest() throws ServiceException {
//        UserResponse expectedUser = UserResponse.builder()
//                .id(ID)
//                .name(NAME)
//                .email(EMAIL)
//                .username(USERNAME)
//                .build();
//        OTP otp = OTP.builder()
//                .id(OTP_ID)
//                .userId(ID)
//                .code(CODE)
//                .createdAt(LocalDateTime.now().plusHours(7))
//                .updatedAt(LocalDateTime.now().plusHours(7))
//                .build();
//        when(userMapper.getUserById(ID)).thenReturn(expectedUser);
//        when(otpMapper.getOTP(ID)).thenReturn(otp);
//        otp.setCode("7272");
//        OTPResponse expectedOTP = new OTPResponse(OTP_ID, ID, "7272");
//        twillioSMS.send(TWILLIO_NUMBER, TWILLIO_SID, TWILLIO_TOKEN, USERNAME, CODE);
//        doNothing().when(otpMapper).updateOTP(otp);
//        when(otpMapper.getOTPResponse(ID)).thenReturn(expectedOTP);
//        assert otpService.sendOTP(ID).equals(expectedOTP);
//        verify(twillioSMS, times(1)).send(TWILLIO_NUMBER, TWILLIO_NUMBER, TWILLIO_TOKEN, USERNAME, CODE);
//        verify(otpMapper, times(1)).updateOTP(otp);
//    }


    @Test
    public void getOTPUserIdNullTest() {
        try {
            otpService.getOTP(0);
        } catch (NullPointerException | ServiceException e) {
            assert e.getMessage().equals("id should not be empty");
        }
    }

    @Test
    public void getOTPUserNotFoundTest() {
        try {
            when(userMapper.getUserById(ID)).thenReturn(null);
            otpService.getOTP(ID);
        } catch (ServiceException e) {
            assert e.getMessage().equals("user not found");
        }
    }

    @Test
    public void getOTPNotFoundTest() {
        UserResponse expectedUser = UserResponse.builder()
                .id(ID)
                .name(NAME)
                .email(EMAIL)
                .username(USERNAME)
                .build();
        try {
            when(userMapper.getUserById(ID)).thenReturn(expectedUser);
            when(otpMapper.getOTPResponse(ID)).thenReturn(null);
            otpService.getOTP(ID);
        } catch (ServiceException e) {
            assert e.getMessage().equals("OTP not found");
        }
    }

    @Test
    public void getOTPSuccessTest() throws ServiceException {
        UserResponse expectedUser = UserResponse.builder()
                .id(ID)
                .name(NAME)
                .email(EMAIL)
                .username(USERNAME)
                .build();

        OTPResponse expectedOTP = new OTPResponse(OTP_ID, ID, CODE);

        when(userMapper.getUserById(ID)).thenReturn(expectedUser);
        when(otpMapper.getOTPResponse(ID)).thenReturn(expectedOTP);
        assert otpService.getOTP(ID).equals(expectedOTP);
    }


    @Test
    public void verifyOTPUserIdNullTest() {
        try {
            otpService.verifyOTP(0, CODE);
        } catch (NullPointerException | ServiceException e) {
            assert e.getMessage().equals("id should not be empty");
        }
    }

    @Test
    public void verifyOTPCodeNullTest() {
        try {
            otpService.verifyOTP(ID, null);
        } catch (NullPointerException | ServiceException e) {
            assert e.getMessage().equals("OTP code should not be empty");
        }
    }

    @Test
    public void verifyOTPCodeEmptyStringTest() {
        try {
            otpService.verifyOTP(ID, "");
        } catch (NullPointerException | ServiceException e) {
            assert e.getMessage().equals("OTP code should not be empty");
        }
    }

    @Test
    public void verifyOTPFailedTest() {
        try {
            when(validation.validateOtp(CODE)).thenReturn(true);
            when(otpMapper.getVerifyOTP(ID, CODE)).thenReturn(null);
            otpService.verifyOTP(ID, CODE);
        } catch (ServiceException e) {
            assert e.getMessage().equals("incorrect OTP");
        }
    }

    @Test
    public void verifyOTPExpiredTest() {
        OTP otp = OTP.builder()
                .id(OTP_ID)
                .userId(ID)
                .code(CODE)
                .createdAt(LocalDateTime.now().minusHours(4))
                .updatedAt(LocalDateTime.now().minusHours(3))
                .build();
        try {
            when(validation.validateOtp(CODE)).thenReturn(true);
            when(otpMapper.getVerifyOTP(ID, CODE)).thenReturn(otp);
            otp.getUpdatedAt().plusMinutes(3).isBefore(LocalDateTime.now());
            otpService.verifyOTP(ID, CODE);
        } catch (ServiceException e) {
            assert e.getMessage().equals("OTP expired");
        }
    }

    @Test
    public void verifyOTPSuccessTest() throws ServiceException {
        OTP otp = OTP.builder()
                .id(OTP_ID)
                .userId(ID)
                .code(CODE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        OTPResponse expectedOTP = new OTPResponse(OTP_ID, ID, CODE);
        when(validation.validateOtp(CODE)).thenReturn(true);
        when(otpMapper.getVerifyOTP(ID, CODE)).thenReturn(otp);
        when(otpMapper.getOTPResponse(ID)).thenReturn(expectedOTP);
        assert otpService.verifyOTP(ID, CODE).equals(expectedOTP);
    }





}
