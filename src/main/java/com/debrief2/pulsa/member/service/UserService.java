package com.debrief2.pulsa.member.service;

import com.debrief2.pulsa.member.exception.ServiceException;
import com.debrief2.pulsa.member.model.User;
import com.debrief2.pulsa.member.payload.request.UserRequest;
import com.debrief2.pulsa.member.payload.response.UserResponse;

public interface UserService {
    User getByEmailOrUsername(String emailOrUsername);
    UserResponse register(UserRequest userRequest) throws ServiceException;
    UserResponse login(String phone) throws ServiceException;
    UserResponse verifyPin(long id, String pin) throws ServiceException;
    UserResponse getProfile(long id) throws ServiceException;
    long getBalance(long id) throws ServiceException;
    String changePin(long id, String pin) throws ServiceException;
    String decreaseBalance(long id, long value) throws ServiceException;
    String increaseBalance(long id, long value) throws ServiceException;
}
