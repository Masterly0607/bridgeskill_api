package com.aditi_final.bridgeskill_api.dto.student_profile;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStudentProfileRequest {

    @Size(max = 1000, message = "Bio must not exceed 1000 characters")
    private String bio;

    @Size(max = 1000, message = "Skills must not exceed 1000 characters")
    private String skills;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;

    @Size(max = 150, message = "University must not exceed 150 characters")
    private String university;
}