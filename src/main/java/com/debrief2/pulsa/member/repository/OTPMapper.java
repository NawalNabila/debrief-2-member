package com.debrief2.pulsa.member.repository;

import com.debrief2.pulsa.member.model.OTP;
import com.debrief2.pulsa.member.payload.response.OTPResponse;
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

    @Select("SELECT * FROM otp WHERE userId = #{userId}")
    OTPResponse getOTPResponse(long userId);

    @Select("SELECT * FROM otp WHERE userId = #{userId} AND code = #{code}")
    OTP getVerifyOTP(long userId, String code);

    @Insert("INSERT INTO otp (userId, code, createdAt, updatedAt) VALUES (#{userId}, #{code}, #{createdAt}, #{updatedAt})")
    void createOTP(OTP otp);

    @Update("UPDATE otp SET code = #{code}, updatedAt = #{updatedAt} WHERE userId = #{userId}")
    void updateOTP(OTP otp);
}
