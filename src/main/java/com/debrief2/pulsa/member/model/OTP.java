package com.debrief2.pulsa.member.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OTP {
    private long id;
    private long userId;
    private String code;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
