package com.debrief2.pulsa.member.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BalanceMapper {
    @Insert("INSERT INTO balance (userId, balance, createdAt) VALUES (#{userId}, 15000000, NOW())")
    void createBalance(long userId);

    @Select("SELECT balance FROM balance WHERE userId = #{userId} FOR UPDATE")
    long getBalance(long userId);

    @Update("UPDATE balance SET balance = balance -  #{value}, updatedAt = NOW() WHERE userId = #{userId}")
    void decreaseBalance(long userId, long value);

    @Update("UPDATE balance SET balance = balance +  #{value}, updatedAt = NOW() WHERE userId = #{userId}")
    void increaseBalance(long userId, long value);
}
