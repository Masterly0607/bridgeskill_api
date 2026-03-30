package com.aditi_final.bridgeskill_api.dto.student_profile;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StudentProfileResponse {
    private Long id;
    private Long userId;
    private String bio;
    private String skills;
    private String phone;
    private String university;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}