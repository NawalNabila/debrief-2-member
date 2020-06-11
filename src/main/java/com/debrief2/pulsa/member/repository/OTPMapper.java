package com.debrief2.pulsa.member.repository;

import com.debrief2.pulsa.member.model.OTP;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OTPMapper {
    @Select("SELECT * FROM otp WHERE userId = #{userId}")
    OTP getOTP(long userId);

    @Select("SELECT * FROM otp WHERE userId = #{id} AND code = #{code}")
    OTP getVerifyOTP(long userId, String code);

    @Insert("INSERT INTO otp (userId, code, createdAt, updatedAt) VALUES (#{userId}, #{code}, NOW(), NOW())")
    void createOTP(OTP otp);

    @Update("UPDATE otp SET code = #{code}, updatedAt = NOW() WHERE userId = #{userId}")
    void updateOTP(OTP otp);
}
