package com.aditi_final.bridgeskill_api.service;

import com.aditi_final.bridgeskill_api.dto.student_profile.CreateStudentProfileRequest;
import com.aditi_final.bridgeskill_api.dto.student_profile.StudentProfileResponse;
import com.aditi_final.bridgeskill_api.dto.student_profile.UpdateStudentProfileRequest;
import com.aditi_final.bridgeskill_api.entity.StudentProfile;
import com.aditi_final.bridgeskill_api.entity.User;
import com.aditi_final.bridgeskill_api.exception.DuplicateResourceException;
import com.aditi_final.bridgeskill_api.exception.ResourceNotFoundException;
import com.aditi_final.bridgeskill_api.repository.StudentProfileRepository;
import com.aditi_final.bridgeskill_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;
    private final UserRepository userRepository;

    public StudentProfileResponse createProfile(CreateStudentProfileRequest request) {
        User currentUser = getCurrentUser();

        if (studentProfileRepository.existsByUserId(currentUser.getId())) {
            throw new DuplicateResourceException("Student profile already exists");
        }

        StudentProfile profile = StudentProfile.builder()
                .userId(currentUser.getId())
                .bio(request.getBio())
                .skills(request.getSkills())
                .phone(request.getPhone())
                .university(request.getUniversity())
                .build();

        StudentProfile savedProfile = studentProfileRepository.save(profile);

        return mapToResponse(savedProfile);
    }

    public StudentProfileResponse getMyProfile() {
        User currentUser = getCurrentUser();

        StudentProfile profile = studentProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found"));

        return mapToResponse(profile);
    }

    public StudentProfileResponse updateProfile(UpdateStudentProfileRequest request) {
        User currentUser = getCurrentUser();

        StudentProfile profile = studentProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found"));

        profile.setBio(request.getBio());
        profile.setSkills(request.getSkills());
        profile.setPhone(request.getPhone());
        profile.setUniversity(request.getUniversity());

        StudentProfile updatedProfile = studentProfileRepository.save(profile);

        return mapToResponse(updatedProfile);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private StudentProfileResponse mapToResponse(StudentProfile profile) {
        return StudentProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .bio(profile.getBio())
                .skills(profile.getSkills())
                .phone(profile.getPhone())
                .university(profile.getUniversity())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
}