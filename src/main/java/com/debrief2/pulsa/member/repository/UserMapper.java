package com.debrief2.pulsa.member.repository;

import com.debrief2.pulsa.member.model.User;
import com.debrief2.pulsa.member.payload.request.UserRequest;
import com.debrief2.pulsa.member.payload.response.UserResponse;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    @Select("SELECT * FROM user WHERE email = #{emailOrUsername} OR username = #{emailOrUsername} ORDER BY id DESC LIMIT 1")
    User getUserByEmailOrUsername(String emailOrUsername);

    @Insert("INSERT INTO user (name, email, username, pin, createdAt) VALUES (#{name}, #{email}, #{phone}, #{pin}, NOW())")
    void createUser(UserRequest userRequest);

    @Select("SELECT * FROM user WHERE username = #{username} ORDER BY id DESC LIMIT 1")
    UserResponse getUserByUsername(String username);

    @Select("SELECT * FROM user WHERE id = #{id} AND pin = #{pin}")
    UserResponse getPin(long id, int pin);

    @Select("SELECT * FROM user WHERE id = #{id}")
    UserResponse getUserById(long id);

    @Update("UPDATE user SET pin = #{pin}, updatedAt = NOW() WHERE id = #{id}")
    void updatePin(long id, int pin);
}
