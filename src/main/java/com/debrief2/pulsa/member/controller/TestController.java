package com.debrief2.pulsa.member.controller;

import com.debrief2.pulsa.member.exception.ServiceException;
import com.debrief2.pulsa.member.payload.request.*;
import com.debrief2.pulsa.member.payload.response.OTPResponse;
import com.debrief2.pulsa.member.payload.response.UserResponse;
import com.debrief2.pulsa.member.service.OTPService;
import com.debrief2.pulsa.member.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    UserService userService;

    @Autowired
    OTPService otpService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/member")
    public String apiTester(@RequestBody TesterRequest testerRequest) {
        String response = "";
        try {
            switch (testerRequest.getQueue()) {
                case "register":
                    UserRequest registerRequest = objectMapper.readValue(testerRequest.getMessage(), UserRequest.class);
                    UserResponse registerResponse = userService.register(registerRequest);
                    response = objectMapper.writeValueAsString(registerResponse);
                    break;
                case "login":
                    UserResponse loginResponse = userService.login(testerRequest.getMessage());
                    response = objectMapper.writeValueAsString(loginResponse);
                    break;
                case "verifyPin":
                    VerifyPinRequest pinRequest = objectMapper.readValue(testerRequest.getMessage(), VerifyPinRequest.class);
                    UserResponse verifyPinResponse = userService.verifyPin(pinRequest.getId(), pinRequest.getPin());
                    response = objectMapper.writeValueAsString(verifyPinResponse);
                    break;
                case "getProfile":
                    UserResponse profileResponse = userService.getProfile(Long.parseLong(testerRequest.getMessage()));
                    response = objectMapper.writeValueAsString(profileResponse);
                    break;
                case "getBalance":
                    long balanceResponse = userService.getBalance(Long.parseLong(testerRequest.getMessage()));
                    response = objectMapper.writeValueAsString(Long.toString(balanceResponse));
                    break;
                case "decreaseBalance":
                    BalanceRequest decreaseRequest = objectMapper.readValue(testerRequest.getMessage(), BalanceRequest.class);
                    String decrease = userService.decreaseBalance(decreaseRequest.getId(), decreaseRequest.getValue());
                    response = objectMapper.writeValueAsString(decrease);
                    break;
                case "increaseBalance":
                    BalanceRequest increaseRequest = objectMapper.readValue(testerRequest.getMessage(), BalanceRequest.class);
                    userService.increaseBalance(increaseRequest.getId(), increaseRequest.getValue());
                    break;
                case "sendOTP":
                    OTPResponse otpResponse = otpService.sendOTP(Long.parseLong(testerRequest.getMessage()));
                    response = objectMapper.writeValueAsString(otpResponse);
                    break;
                case "getOTP":
                    OTPResponse getOTPResponse = otpService.getOTP(Long.parseLong(testerRequest.getMessage()));
                    response = objectMapper.writeValueAsString(getOTPResponse);
                    break;
                case "verifyOTP":
                    OTPRequest otpRequest = objectMapper.readValue(testerRequest.getMessage(), OTPRequest.class);
                    OTPResponse verifyOTPResponse = otpService.verifyOTP(otpRequest.getId(), otpRequest.getCode());
                    response = objectMapper.writeValueAsString(verifyOTPResponse);
                    break;
                case "changePin":
                    VerifyPinRequest pin = objectMapper.readValue(testerRequest.getMessage(), VerifyPinRequest.class);
                    String changePinResponse = userService.changePin(pin.getId(), pin.getPin());
                    response = objectMapper.writeValueAsString(changePinResponse);
                    break;
                default:
                    response = "Unknown service method";
                    break;
            }
        } catch (ServiceException | NullPointerException exception) {
            response = exception.getMessage();
        } catch (InvalidFormatException | NumberFormatException e) {
            response = "invalid request format";
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return response;
    }

}
