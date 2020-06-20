package com.debrief2.pulsa.member.service;

import com.debrief2.pulsa.member.exception.ServiceException;
import com.debrief2.pulsa.member.model.User;
import com.debrief2.pulsa.member.payload.request.UserRequest;
import com.debrief2.pulsa.member.payload.response.UserResponse;
import com.debrief2.pulsa.member.repository.BalanceMapper;
import com.debrief2.pulsa.member.repository.UserMapper;
import com.debrief2.pulsa.member.service.impl.UserServiceImpl;
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
public class UserServiceTest {

    @Mock
    private UserMapper userMapper;
    @Mock
    private BalanceMapper balanceMapper;
    @Mock
    private Validation validation;

    @InjectMocks
    private UserServiceImpl userService;

    private static final long ID = 1;
    private static final String NAME = "Abigail";
    private static final String EMAIL = "abigail@dana.id";
    private static final String INVALID_EMAIL = "abigail@dana";
    private static final String USERNAME = "082272068810";
    private static final String INVALID_USERNAME = "08227206";
    private static final String PIN = "123456";
    private static final String INVALID_PIN = "0123456";
    private static final LocalDateTime CREATED_AT = LocalDateTime.now();
    private static final long VALUE = 2000000;
    private static final long BALANCE = 15000000;


    @Test
    public void registerNameEmptyStringTest() throws ServiceException {
        UserRequest request = new UserRequest();
        request.setName("");
        request.setEmail(EMAIL);
        request.setPhone(USERNAME);
        request.setPin(PIN);

        try {
            userService.register(request);
        } catch (NullPointerException e) {
            assert e.getMessage().equals("Name should not be empty");
        }
    }

    @Test
    public void registerNameNullTest() throws ServiceException {
        UserRequest request = new UserRequest();
        request.setName(null);
        request.setEmail(EMAIL);
        request.setPhone(USERNAME);
        request.setPin(PIN);

        try {
            userService.register(request);
        } catch (NullPointerException e) {
            assert e.getMessage().equals("Name should not be empty");
        }
    }

    @Test
    public void registerEmailNullTest() throws ServiceException {
        UserRequest request = new UserRequest();
        request.setName(NAME);
        request.setEmail(null);
        request.setPhone(USERNAME);
        request.setPin(PIN);

        try {
            userService.register(request);
        } catch (NullPointerException e) {
            assert e.getMessage().equals("Email should not be empty");
        }
    }

    @Test
    public void registerEmailEmptyStringTest() throws ServiceException {
        UserRequest request = new UserRequest();
        request.setName(NAME);
        request.setEmail("");
        request.setPhone(USERNAME);
        request.setPin(PIN);

        try {
            userService.register(request);
        } catch (NullPointerException e) {
            assert e.getMessage().equals("Email should not be empty");
        }
    }

    @Test
    public void registerUsernameNullTest() throws ServiceException {
        UserRequest request = new UserRequest();
        request.setName(NAME);
        request.setEmail(EMAIL);
        request.setPhone(null);
        request.setPin(PIN);

        try {
            userService.register(request);
        } catch (NullPointerException e) {
            assert e.getMessage().equals("phone number should not be empty");
        }
    }

    @Test
    public void registerUsernameEmptyStringTest() throws ServiceException {
        UserRequest request = new UserRequest();
        request.setName(NAME);
        request.setEmail(EMAIL);
        request.setPhone("");
        request.setPin(PIN);

        try {
            userService.register(request);
        } catch (NullPointerException e) {
            assert e.getMessage().equals("phone number should not be empty");
        }
    }

    @Test
    public void registerPinNullTest() throws ServiceException {
        UserRequest request = new UserRequest();
        request.setName(NAME);
        request.setEmail(EMAIL);
        request.setPhone(USERNAME);
        request.setPin(null);

        try {
            userService.register(request);
        } catch (NullPointerException e) {
            assert e.getMessage().equals("pin should not be empty");
        }
    }

    @Test
    public void registerPinEmptyStringTest() throws ServiceException {
        UserRequest request = new UserRequest();
        request.setName(NAME);
        request.setEmail(EMAIL);
        request.setPhone(USERNAME);
        request.setPin("");

        try {
            userService.register(request);
        } catch (NullPointerException e) {
            assert e.getMessage().equals("pin should not be empty");
        }
    }

    @Test
    public void registerUserExistTest() {
        UserRequest request = UserRequest.builder()
                .name(NAME)
                .email(EMAIL)
                .phone(USERNAME)
                .pin(PIN)
                .build();
        String validUsername = "6282272068810";

        try {
            User user = new User(ID, NAME, EMAIL, validUsername, PIN, CREATED_AT, null);
            when(validation.convertPhone(USERNAME)).thenReturn(validUsername);
            when(validation.validateUser(EMAIL, validUsername, PIN)).thenReturn(true);
            when(userMapper.getUserByEmailOrUsername(EMAIL)).thenReturn(user);
            userService.register(request);
        } catch (ServiceException e) {
            assert e.getMessage().equals("user already exist");
        }
    }

    @Test
    public void registerInvalidEmailTest() {
        UserRequest request = UserRequest.builder()
                .name(NAME)
                .email(INVALID_EMAIL)
                .phone(USERNAME)
                .pin(PIN)
                .build();
        String validUsername = "6282272068810";

        try {
            when(validation.convertPhone(USERNAME)).thenReturn(validUsername);
            when(validation.validateUser(INVALID_EMAIL, validUsername, PIN)).thenReturn(false);
            userService.register(request);
        } catch (ServiceException e) {
            assert e.getMessage().equals("invalid email");
        }
    }

    @Test
    public void registerInvalidUsernameTest() {
        UserRequest request = UserRequest.builder()
                .name(NAME)
                .email(EMAIL)
                .phone(INVALID_USERNAME)
                .pin(PIN)
                .build();
        String validUsername = "628227206";

        try {
            when(validation.convertPhone(INVALID_USERNAME)).thenReturn(validUsername);
            when(validation.validateUser(EMAIL, validUsername, PIN)).thenReturn(false);
            userService.register(request);
        } catch (ServiceException e) {
            assert e.getMessage().equals("invalid phone number");
        }
    }

    @Test
    public void registerInvalidPinTest() {
        UserRequest request = UserRequest.builder()
                .name(NAME)
                .email(EMAIL)
                .phone(USERNAME)
                .pin(INVALID_PIN)
                .build();
        String validUsername = "6282272068810";

        try {
            when(validation.convertPhone(USERNAME)).thenReturn(validUsername);
            when(validation.validateUser(EMAIL, validUsername, INVALID_PIN)).thenReturn(false);
            userService.register(request);
        } catch (ServiceException e) {
            assert e.getMessage().equals("invalid pin");
        }
    }

    @Test
    public void registerSuccessTest() throws ServiceException {
        UserRequest request = UserRequest.builder()
                .name(NAME)
                .email(EMAIL)
                .phone(USERNAME)
                .pin(PIN)
                .build();
        String validUsername = "6282272068810";

        when(validation.convertPhone(USERNAME)).thenReturn(validUsername);
        when(validation.validateUser(EMAIL, validUsername, PIN)).thenReturn(true);
        when(userMapper.getUserByEmailOrUsername(validUsername)).thenReturn(null);
        when(userMapper.getUserByEmailOrUsername(EMAIL)).thenReturn(null);

        UserRequest dto = UserRequest.builder()
                .name(NAME)
                .email(EMAIL)
                .phone(validUsername)
                .pin(PIN)
                .build();

        doNothing().when(userMapper).createUser(dto);

        UserResponse response = new UserResponse(ID, NAME, EMAIL, validUsername);
        when(userMapper.getUserByUsername(validUsername)).thenReturn(response);
        doNothing().when(balanceMapper).createBalance(ID);

        assert userService.register(request).equals(response);

        verify(userMapper, times(1)).createUser(dto);
        verify(balanceMapper, times(1)).createBalance(ID);
    }

    @Test
    public void loginUsernameNullTest() throws ServiceException {
        try {
            userService.login(null);
        } catch (NullPointerException e) {
            assert e.getMessage().equals("phone number should not be empty");
        }
    }

    @Test
    public void loginUsernameEmptyStringTest() throws ServiceException {
        try {
            userService.login("");
        } catch (NullPointerException e) {
            assert e.getMessage().equals("phone number should not be empty");
        }
    }

    @Test
    public void loginFailedTest() {
        String validUsername = "6282272068810";

        try {
            when(validation.convertPhone(USERNAME)).thenReturn(validUsername);
            when(validation.validatePhone(validUsername)).thenReturn(true);
            when(userMapper.getUserByUsername(validUsername)).thenReturn(null);
            userService.login(USERNAME);
        } catch (ServiceException e) {
            assert e.getMessage().equals("incorrect phone number");
        }
    }


    @Test
    public void loginSuccessTest() throws ServiceException {
        String validUsername = "6282272068810";

        UserResponse expected = UserResponse.builder()
                .id(ID)
                .name(NAME)
                .email(EMAIL)
                .username(validUsername)
                .build();

        when(validation.convertPhone(USERNAME)).thenReturn(validUsername);
        when(validation.validatePhone(validUsername)).thenReturn(true);
        when(userMapper.getUserByUsername(validUsername)).thenReturn(expected);
        assert userService.login(USERNAME).equals(expected);
    }

    @Test
    public void verifyPinIdNullTest() throws ServiceException {
        try {
            userService.verifyPin(0, PIN);
        } catch (NullPointerException e) {
            assert e.getMessage().equals("id should not be empty");
        }
    }

    @Test
    public void verifyPinNullPinTest() throws ServiceException {
        try {
            userService.verifyPin(ID, null);
        } catch (NullPointerException e) {
            assert e.getMessage().equals("pin should not be empty");
        }
    }

    @Test
    public void verifyPinEmptyStringPinTest() throws ServiceException {
        try {
            userService.verifyPin(ID, "");
        } catch (NullPointerException e) {
            assert e.getMessage().equals("pin should not be empty");
        }
    }

    @Test
    public void verifyPinFailedTest() {
        try {
            when(validation.validatePin(PIN)).thenReturn(true);
            when(userMapper.getPin(ID, PIN)).thenReturn(null);
            userService.verifyPin(ID, PIN);
        } catch (ServiceException e) {
            assert e.getMessage().equals("incorrect pin");
        }
    }

    @Test
    public void verifyPinSuccessTest() throws ServiceException {
        UserResponse expected = UserResponse.builder()
                .id(ID)
                .name(NAME)
                .email(EMAIL)
                .username("6282272068810")
                .build();

        when(validation.validatePin(PIN)).thenReturn(true);
        when(userMapper.getPin(ID, PIN)).thenReturn(expected);
        assert userService.verifyPin(ID, PIN).equals(expected);
    }

    @Test
    public void getProfileIdNullTest() throws ServiceException {
        try {
            userService.getProfile(0);
        } catch (NullPointerException e) {
            assert e.getMessage().equals("id should not be empty");
        }
    }

    @Test
    public void getProfileFailedTest() {
        try {
            when(userMapper.getUserById(ID)).thenReturn(null);
            userService.getProfile(ID);
        } catch (ServiceException e) {
            assert e.getMessage().equals("user not found");
        }
    }

    @Test
    public void getProfileSuccessTest() throws ServiceException {
        UserResponse expected = UserResponse.builder()
                .id(ID)
                .name(NAME)
                .email(EMAIL)
                .username("6282272068810")
                .build();

        when(userMapper.getUserById(ID)).thenReturn(expected);
        assert userService.getProfile(ID).equals(expected);
    }

    @Test
    public void getBalanceIdNullTest() throws ServiceException {
        try {
            userService.getBalance(0);
        } catch (NullPointerException e) {
            assert e.getMessage().equals("id should not be empty");
        }
    }

    @Test
    public void getBalanceUserNotFoundInUserTest() {
        try {
            when(userMapper.getUserById(ID)).thenReturn(null);
            userService.getBalance(ID);
        } catch (ServiceException e) {
            assert e.getMessage().equals("user not found");
        }
    }

    @Test
    public void getBalanceUserNotFoundInBalanceTest() {
        UserResponse expected = UserResponse.builder()
                .id(ID)
                .name(NAME)
                .email(EMAIL)
                .username("6282272068810")
                .build();

        try {
            when(userMapper.getUserById(ID)).thenReturn(expected);
            when(balanceMapper.getUserInBalance(ID)).thenReturn(null);
            userService.getBalance(ID);
        } catch (ServiceException e) {
            assert e.getMessage().equals("Bikin datanya di table balance dulu bang!");
        }
    }

    @Test
    public void getBalanceSuccessTest() throws ServiceException {
        UserResponse expected = UserResponse.builder()
                .id(ID)
                .name(NAME)
                .email(EMAIL)
                .username("6282272068810")
                .build();

        User user = User.builder()
                .id(ID)
                .name(NAME)
                .email(EMAIL)
                .username("6282272068810")
                .createdAt(CREATED_AT)
                .updatedAt(null)
                .build();

        when(userMapper.getUserById(ID)).thenReturn(expected);
        when(balanceMapper.getUserInBalance(ID)).thenReturn(user);
        when(balanceMapper.getBalance(ID)).thenReturn((long) 15000000);
        userService.getBalance(ID);
    }

    @Test
    public void changePinIdNullTest() throws ServiceException {
        try {
            userService.changePin(0, PIN);
        } catch (NullPointerException e) {
            assert e.getMessage().equals("id should not be empty");
        }
    }

    @Test
    public void changePinNullPinTest() throws ServiceException {
        try {
            userService.changePin(ID, null);
        } catch (NullPointerException e) {
            assert e.getMessage().equals("pin should not be empty");
        }
    }

    @Test
    public void changePinEmptyStringPinTest() throws ServiceException {
        try {
            userService.changePin(ID, "");
        } catch (NullPointerException e) {
            assert e.getMessage().equals("pin should not be empty");
        }
    }

    @Test
    public void changePinFailedTest() {
        try {
            when(validation.validatePin(PIN)).thenReturn(true);
            when(userMapper.getUserById(ID)).thenReturn(null);
            userService.changePin(ID, PIN);
        } catch (ServiceException e) {
            assert e.getMessage().equals("user not found");
        }
    }

    @Test
    public void changePinSuccessTest() {
        UserResponse expected = UserResponse.builder()
                .id(ID)
                .name(NAME)
                .email(EMAIL)
                .username("6282272068810")
                .build();

        try {
            when(validation.validatePin(PIN)).thenReturn(true);
            when(userMapper.getUserById(ID)).thenReturn(expected);
            doNothing().when(userMapper).updatePin(ID, PIN);
            userService.changePin(ID, PIN);
        } catch (ServiceException e) {
            assert e.getMessage().equals("updated");
            verify(userMapper, times(1)).updatePin(ID, PIN);
        }
    }

    @Test
    public void decreaseBalanceIdNullTest() throws ServiceException {
        try {
            userService.decreaseBalance(0, VALUE);
        } catch (NullPointerException e) {
            assert e.getMessage().equals("id should not be empty");
        }
    }

    @Test
    public void decreaseBalanceValueMinusTest() {
        try {
            userService.decreaseBalance(ID, -2000);
        } catch (ServiceException e) {
            assert e.getMessage().equals("value should not be under zero");
        }
    }

    @Test
    public void decreaseBalanceUserNotFoundTest() {
        try {
            when(userMapper.getUserById(ID)).thenReturn(null);
            userService.decreaseBalance(ID, VALUE);
        } catch (ServiceException e) {
            assert e.getMessage().equals("user not found");
        }
    }

    @Test
    public void decreaseBalanceNotEnoughBalanceTest() {
        UserResponse expected = UserResponse.builder()
                .id(ID)
                .name(NAME)
                .email(EMAIL)
                .username("6282272068810")
                .build();

        try {
            when(userMapper.getUserById(ID)).thenReturn(expected);
            when(balanceMapper.getBalance(ID)).thenReturn(BALANCE);
            userService.decreaseBalance(ID, 16000000);
        } catch (ServiceException e) {
            assert e.getMessage().equals("not enough balance");
        }
    }

    @Test
    public void decreaseBalanceSuccessTest() {
        UserResponse expected = UserResponse.builder()
                .id(ID)
                .name(NAME)
                .email(EMAIL)
                .username("6282272068810")
                .build();

        try {
            when(userMapper.getUserById(ID)).thenReturn(expected);
            when(balanceMapper.getBalance(ID)).thenReturn(BALANCE);
            doNothing().when(balanceMapper).decreaseBalance(ID, VALUE);
            userService.decreaseBalance(ID, VALUE);
        } catch (ServiceException e) {
            assert e.getMessage().equals("success");
            verify(balanceMapper, times(1)).decreaseBalance(ID, VALUE);
        }
    }

    @Test
    public void increaseBalanceIdNullTest() throws ServiceException {
        try {
            userService.increaseBalance(0, VALUE);
        } catch (NullPointerException e) {
            assert e.getMessage().equals("id should not be empty");
        }
    }

    @Test
    public void increaseBalanceValueMinusTest() {
        try {
            userService.increaseBalance(ID, -2000);
        } catch (ServiceException e) {
            assert e.getMessage().equals("value should not be under zero");
        }
    }

    @Test
    public void increaseBalanceUserNotFoundTest() {
        try {
            when(userMapper.getUserById(ID)).thenReturn(null);
            userService.increaseBalance(ID, VALUE);
        } catch (ServiceException e) {
            assert e.getMessage().equals("user not found");
        }
    }

    @Test
    public void increaseBalanceSuccessTest() {
        UserResponse expected = UserResponse.builder()
                .id(ID)
                .name(NAME)
                .email(EMAIL)
                .username("6282272068810")
                .build();

        try {
            when(userMapper.getUserById(ID)).thenReturn(expected);
            doNothing().when(balanceMapper).increaseBalance(ID, VALUE);
            userService.increaseBalance(ID, VALUE);
        } catch (ServiceException e) {
            assert e.getMessage().equals("success");
            verify(balanceMapper, times(1)).increaseBalance(ID, VALUE);
        }
    }
}