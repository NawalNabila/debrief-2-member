package com.debrief2.pulsa.member.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyPinRequest {
    private long id;
    private int pin;
}
