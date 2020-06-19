package com.debrief2.pulsa.member.utils;

import com.debrief2.pulsa.member.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ValidationTest {
    @InjectMocks
    private Validation validation;

    @Test
    public void validEmailTest() throws ServiceException {
        assert validation.validateEmail("abigail@dana.id");
    }

    @Test
    public void invalidEmailTest() {
        try {
            validation.validateEmail("abigail.id");
        } catch (ServiceException e){
            assert e.getMessage().equals("invalid email");
        }

        try {
            validation.validateEmail("abigail@dana");
        } catch (ServiceException e){
            assert e.getMessage().equals("invalid email");
        }

        try {
            validation.validateEmail("abigail");
        } catch (ServiceException e){
            assert e.getMessage().equals("invalid email");
        }
    }

    @Test
    public void validPinTest() throws ServiceException {
        assert validation.validatePin("123456");
    }

    @Test
    public void invalidPinTest() {
        try {
            validation.validatePin("123 456");
        } catch (ServiceException e){
            assert e.getMessage().equals("invalid pin");
        }

        try {
            validation.validatePin("1234567");
        } catch (ServiceException e){
            assert e.getMessage().equals("invalid pin");
        }

        try {
            validation.validatePin("12345");
        } catch (ServiceException e){
            assert e.getMessage().equals("invalid pin");
        }

        try {
            validation.validatePin("098765");
        } catch (ServiceException e){
            assert e.getMessage().equals("invalid pin");
        }
    }

    @Test
    public void validOTPTest() throws ServiceException {
        assert validation.validateOtp("9582");
    }

    @Test
    public void invalidOTPTest() {
        try {
            validation.validateOtp("958");
        } catch (ServiceException e){
            assert e.getMessage().equals("invalid OTP");
        }

        try {
            validation.validateOtp("95824");
        } catch (ServiceException e){
            assert e.getMessage().equals("invalid OTP");
        }

        try {
            validation.validateOtp("982A");
        } catch (ServiceException e){
            assert e.getMessage().equals("invalid OTP");
        }
    }

    @Test
    public void validPhoneTest() throws ServiceException {
        assert validation.validatePhone("628595828593");
    }

    @Test
    public void invalidPhoneTest() {
        try {
            validation.validatePhone("628595822");
        } catch (ServiceException e){
            assert e.getMessage().equals("invalid phone number");
        }

        try {
            validation.validatePhone("628595828294591");
        } catch (ServiceException e){
            assert e.getMessage().equals("invalid phone number");
        }

        try {
            validation.validatePhone("6285958282AA");
        } catch (ServiceException e){
            assert e.getMessage().equals("invalid phone number");
        }
    }

    @Test
    public void convertPhoneTest() {
        assert validation.convertPhone("08595828593").equals("628595828593");
        assert validation.convertPhone("+628595828593").equals("628595828593");
        assert validation.convertPhone("628595828593").equals("628595828593");
    }

    @Test
    public void validateUserTest() throws ServiceException {
        assert validation.validateUser("abigail@dana.id", "6282272068810", "123456");
    }

}
