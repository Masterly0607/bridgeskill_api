package com.aditi_final.bridgeskill_api.controller;

import com.aditi_final.bridgeskill_api.dto.student_profile.CreateStudentProfileRequest;
import com.aditi_final.bridgeskill_api.dto.student_profile.StudentProfileResponse;
import com.aditi_final.bridgeskill_api.dto.student_profile.UpdateStudentProfileRequest;
import com.aditi_final.bridgeskill_api.service.StudentProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student-profile")
@RequiredArgsConstructor
public class StudentProfileController {

    private final StudentProfileService studentProfileService;

    @PostMapping
    public ResponseEntity<StudentProfileResponse> createProfile(
            @Valid @RequestBody CreateStudentProfileRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(studentProfileService.createProfile(request));
    }

    @GetMapping("/me")
    public ResponseEntity<StudentProfileResponse> getMyProfile() {
        return ResponseEntity.ok(studentProfileService.getMyProfile());
    }

    @PutMapping
    public ResponseEntity<StudentProfileResponse> updateProfile(
            @Valid @RequestBody UpdateStudentProfileRequest request
    ) {
        return ResponseEntity.ok(studentProfileService.updateProfile(request));
    }
}