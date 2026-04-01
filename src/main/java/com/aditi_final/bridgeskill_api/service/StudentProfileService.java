package com.aditi_final.bridgeskill_api.service;

import com.aditi_final.bridgeskill_api.dto.student_profile.CreateStudentProfileRequest;
import com.aditi_final.bridgeskill_api.dto.student_profile.StudentProfileResponse;
import com.aditi_final.bridgeskill_api.dto.student_profile.UpdateStudentProfileRequest;
import com.aditi_final.bridgeskill_api.entity.Application;
import com.aditi_final.bridgeskill_api.entity.Job;
import com.aditi_final.bridgeskill_api.entity.StudentProfile;
import com.aditi_final.bridgeskill_api.entity.User;
import com.aditi_final.bridgeskill_api.exception.DuplicateResourceException;
import com.aditi_final.bridgeskill_api.exception.ResourceNotFoundException;
import com.aditi_final.bridgeskill_api.repository.ApplicationRepository;
import com.aditi_final.bridgeskill_api.repository.JobRepository;
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
    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;

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

    public StudentProfileResponse getStudentProfileForClient(Long applicationId) {
        User currentUser = getCurrentUser();

        if (!currentUser.getRoleId().equals(3L)) {
            throw new IllegalArgumentException("Only client can view applicant student profile");
        }

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        Job job = jobRepository.findById(application.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        if (!job.getClientId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You are not allowed to view this student profile");
        }

        StudentProfile profile = studentProfileRepository.findByUserId(application.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found"));

        return mapToResponse(profile);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private StudentProfileResponse mapToResponse(StudentProfile profile) {
        User user = userRepository.findById(profile.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return StudentProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .bio(profile.getBio())
                .skills(profile.getSkills())
                .phone(profile.getPhone())
                .university(profile.getUniversity())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
}