package com.debrief2.pulsa.member.service.impl;

import com.debrief2.pulsa.member.exception.ServiceException;
import com.debrief2.pulsa.member.model.User;
import com.debrief2.pulsa.member.payload.request.UserRequest;
import com.debrief2.pulsa.member.payload.response.UserResponse;
import com.debrief2.pulsa.member.repository.BalanceMapper;
import com.debrief2.pulsa.member.repository.UserMapper;
import com.debrief2.pulsa.member.service.UserService;
import com.debrief2.pulsa.member.utils.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private Validation validation;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BalanceMapper balanceMapper;

    @Override
    public User getByEmailOrUsername(String emailOrUsername) {
        return userMapper.getUserByEmailOrUsername(emailOrUsername);
    }

    @Override
    public void createBalance(long id) {
        balanceMapper.createBalance(id);
    }

    @Override
    public UserResponse getByUsername(String username) {
        UserResponse user = userMapper.getUserByUsername(username);
        if (user != null) {
            createBalance(user.getId());
        }
        return user;
    }

    @Override
    public UserResponse register(UserRequest user) throws ServiceException {

        //validate email
        if(!validation.email(user.getEmail())) {
            throw new ServiceException("invalid email");
        }

        //convert & validate phone
        user.setPhone(validation.convertPhone(user.getPhone()));
        if (!validation.phone(user.getPhone())) {
            throw new ServiceException("invalid phone number");
        }

        //validate pin
        if (!validation.pin(Integer.toString(user.getPin()))){
            throw new ServiceException("invalid pin");
        }

        //check email & phone duplication
        if (getByEmailOrUsername(user.getEmail()) != null || getByEmailOrUsername(user.getPhone()) != null) {
            throw new ServiceException("user already exist");
        }

        userMapper.createUser(user);
        return getByUsername(user.getPhone());
    }

    @Override
    public UserResponse login(String phone) throws ServiceException {
        //convert & validate phone
        phone = validation.convertPhone(phone);
        if (!validation.phone(phone)) {
            throw new ServiceException("invalid phone number");
        }

        UserResponse userResponse = userMapper.getUserByUsername(phone);
        if (userResponse == null)
            throw new ServiceException("incorrect phone number");
        return userResponse;
    }

    @Override
    public UserResponse verifyPin(long id, int pin) throws ServiceException {
        //validate pin
        if (!validation.pin(Integer.toString(pin))){
            throw new ServiceException("invalid pin");
        }

        UserResponse userResponse = userMapper.getPin(id, pin);
        if (userResponse == null)
            throw new ServiceException("incorrect pin");
        return userResponse;
    }

    @Override
    public UserResponse getProfile(long id) throws ServiceException{
        UserResponse userResponse = userMapper.getUserById(id);
        if (userResponse == null)
            throw new ServiceException("user not found");
        return userResponse;
    }

    @Override
    public long getBalance(long id) throws ServiceException{
        UserResponse userResponse = userMapper.getUserById(id);
        if (userResponse == null)
            throw new ServiceException("user not found");
        return balanceMapper.getBalance(userResponse.getId());
    }

    @Override
    public User changePin(long id, int pin) {
        return null;
    }

    @Override
    public void decreaseBalance(long id, long value) {
        balanceMapper.decreaseBalance(id, value);
    }

    @Override
    public void increaseBalance(long id, long value) {
        balanceMapper.increaseBalance(id, value);
    }
}
