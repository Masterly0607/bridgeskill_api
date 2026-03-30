package com.aditi_final.bridgeskill_api.service;

import com.aditi_final.bridgeskill_api.dto.application.ApplicationResponse;
import com.aditi_final.bridgeskill_api.dto.application.CreateApplicationRequest;
import com.aditi_final.bridgeskill_api.dto.application.UpdateApplicationStatusRequest;
import com.aditi_final.bridgeskill_api.entity.Application;
import com.aditi_final.bridgeskill_api.entity.Job;
import com.aditi_final.bridgeskill_api.entity.User;
import com.aditi_final.bridgeskill_api.repository.ApplicationRepository;
import com.aditi_final.bridgeskill_api.repository.JobRepository;
import com.aditi_final.bridgeskill_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public ApplicationResponse applyToJob(Long jobId, CreateApplicationRequest request) {
        User currentUser = getCurrentUser();

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        if (applicationRepository.existsByJobIdAndStudentId(jobId, currentUser.getId())) {
            throw new IllegalArgumentException("You have already applied for this job");
        }

        Application application = Application.builder()
                .jobId(jobId)
                .studentId(currentUser.getId())
                .coverLetter(request.getCoverLetter())
                .status("PENDING")
                .build();

        application = applicationRepository.save(application);

        return mapToResponse(application);
    }

    public List<ApplicationResponse> getMyApplications() {
        User currentUser = getCurrentUser();

        return applicationRepository.findByStudentIdOrderByCreatedAtDesc(currentUser.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ApplicationResponse getMyApplicationById(Long id) {
        User currentUser = getCurrentUser();

        Application application = applicationRepository.findByIdAndStudentId(id, currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        return mapToResponse(application);
    }

    public List<ApplicationResponse> getApplicationsForMyJob(Long jobId) {
        User currentUser = getCurrentUser();

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        if (!job.getClientId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You are not allowed to view applications for this job");
        }

        return applicationRepository.findByJobIdOrderByCreatedAtDesc(jobId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ApplicationResponse getApplicationForMyJob(Long applicationId) {
        User currentUser = getCurrentUser();

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        Job job = jobRepository.findById(application.getJobId())
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        if (!job.getClientId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You are not allowed to view this application");
        }

        return mapToResponse(application);
    }

    public ApplicationResponse updateApplicationStatus(Long applicationId, UpdateApplicationStatusRequest request) {
        User currentUser = getCurrentUser();

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        Job job = jobRepository.findById(application.getJobId())
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        if (!job.getClientId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You are not allowed to update this application");
        }

        String status = request.getStatus().trim().toUpperCase();

        if (!List.of("PENDING", "REVIEWED", "SHORTLISTED", "REJECTED", "ACCEPTED").contains(status)) {
            throw new IllegalArgumentException("Invalid application status");
        }

        application.setStatus(status);
        application = applicationRepository.save(application);

        return mapToResponse(application);
    }

    public List<ApplicationResponse> getAllApplications() {
        return applicationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ApplicationResponse mapToResponse(Application application) {
        return ApplicationResponse.builder()
                .id(application.getId())
                .jobId(application.getJobId())
                .studentId(application.getStudentId())
                .coverLetter(application.getCoverLetter())
                .status(application.getStatus())
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}