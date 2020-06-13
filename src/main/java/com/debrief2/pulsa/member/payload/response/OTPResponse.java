package com.debrief2.pulsa.member.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OTPResponse {
    private long id;
    private long userId;
    private String code;
}

