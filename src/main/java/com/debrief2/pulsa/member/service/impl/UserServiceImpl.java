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
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(rollbackFor = ServiceException.class)
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
    public UserResponse createBalance(String username) {
        UserResponse user = userMapper.getUserByUsername(username);
        if (user != null) {
            balanceMapper.createBalance(user.getId());
        }
        return user;
    }

    @Override
    public UserResponse register(UserRequest user) throws ServiceException, NullPointerException {

        String name = user.getName();
        String email = user.getEmail();
        String username = user.getPhone();
        String pin = user.getPin();

        if(name == null)
            throw new NullPointerException("Name should not be empty");
        name = name.trim();
        if (name.length() == 0)
            throw new NullPointerException("Name should not be empty");

        if(email == null)
            throw new NullPointerException("Email should not be empty");
        email = email.trim();
        if (email.length() == 0)
            throw new NullPointerException("Email should not be empty");

        if(username == null)
            throw new NullPointerException("phone number should not be empty");
        username = username.trim();
        if (username.length() == 0)
            throw new NullPointerException("phone number should not be empty");

        if(pin == null)
            throw new NullPointerException("pin should not be empty");
        pin = pin.trim();
        if (pin.length() == 0)
            throw new NullPointerException("pin should not be empty");

        //convert phone number
        String usernameAfterConvert = validation.convertPhone(username);

        //validate email, phone number and pin
        validation.validateUser(email, usernameAfterConvert, pin);


        //check email & phone duplication
        if (getByEmailOrUsername(email) != null || getByEmailOrUsername(usernameAfterConvert) != null) {
            throw new ServiceException("user already exist");
        }

        UserRequest request = new UserRequest();
        request.setName(name);
        request.setEmail(email);
        request.setPhone(usernameAfterConvert);
        request.setPin(pin);
        userMapper.createUser(request);
        return createBalance(usernameAfterConvert);
    }

    @Override
    public UserResponse login(String phone) throws ServiceException, NullPointerException{
        if(phone == null)
            throw new NullPointerException("phone number should not be empty");
        phone = phone.trim();
        if (phone.length() == 0)
            throw new NullPointerException("phone number should not be empty");

        //convert phone number
        String username = validation.convertPhone(phone);

        //validate phone
        validation.validatePhone(username);

        UserResponse userResponse = userMapper.getUserByUsername(username);
        if (userResponse == null)
            throw new ServiceException("incorrect phone number");
        return userResponse;
    }

    @Override
    public UserResponse verifyPin(long id, String pin) throws ServiceException, NullPointerException {

        if(id == 0)
            throw new NullPointerException("id should not be empty");

        if(pin == null)
            throw new NullPointerException("pin should not be empty");
        pin = pin.trim();
        if (pin.length() == 0)
            throw new NullPointerException("pin should not be empty");

        //validate pin
        validation.validatePin(pin);

        UserResponse userResponse = userMapper.getPin(id, pin);
        if (userResponse == null)
            throw new ServiceException("incorrect pin");
        return userResponse;
    }

    @Override
    public UserResponse getProfile(long id) throws ServiceException, NullPointerException{
        if(id == 0)
            throw new NullPointerException("id should not be empty");

        UserResponse userResponse = userMapper.getUserById(id);
        if (userResponse == null)
            throw new ServiceException("user not found");
        return userResponse;
    }

    @Override
    public long getBalance(long id) throws ServiceException, NullPointerException{
        if(id == 0)
            throw new NullPointerException("id should not be empty");

        UserResponse userResponse = userMapper.getUserById(id);
        if (userResponse == null)
            throw new ServiceException("user not found");

        User userInBalance = balanceMapper.getUserInBalance(id);
        if (userInBalance ==  null)
            throw new ServiceException("Bikin datanya di table balance dulu bang!");
        return balanceMapper.getBalance(id);
    }

    @Override
    public User changePin(long id, String pin) throws ServiceException, NullPointerException {
        if(id == 0)
            throw new NullPointerException("id should not be empty");

        if(pin == null)
            throw new NullPointerException("pin should not be empty");
        pin = pin.trim();
        if (pin.length() == 0)
            throw new NullPointerException("pin should not be empty");

        //validate pin
        validation.validatePin(pin);

        UserResponse user = userMapper.getUserById(id);
        if (user != null) {
            userMapper.updatePin(user.getId(), pin);
            throw new ServiceException("updated");
        } else {
            throw new ServiceException("user not found");
        }
    }

    @Override
    public User decreaseBalance(long id, long value) throws ServiceException, NullPointerException{
        if(id == 0)
            throw new NullPointerException("id should not be empty");

        if (value < 0)
            throw new ServiceException("value should not be under zero");

        UserResponse userResponse = userMapper.getUserById(id);
        if (userResponse == null)
            throw new ServiceException("user not found");

        long balance = balanceMapper.getBalance(id);
        if (balance < value)
            throw new ServiceException("not enough balance");

        balanceMapper.decreaseBalance(id, value);
        throw new ServiceException("success");
    }

    @Override
    public User increaseBalance(long id, long value) throws ServiceException, NullPointerException {
        if(id == 0)
            throw new NullPointerException("id should not be empty");

        if (value < 0)
            throw new ServiceException("value should not be under zero");

        UserResponse userResponse = userMapper.getUserById(id);
        if (userResponse == null)
            throw new ServiceException("user not found");

        balanceMapper.increaseBalance(id, value);
        throw new ServiceException("success");
    }
}
