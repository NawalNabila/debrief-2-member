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
public class User {
    private long id;
    private String name;
    private String email;
    private String username;
    private int pin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
