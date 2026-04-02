package com.aditi_final.bridgeskill_api.service;

import com.aditi_final.bridgeskill_api.dto.admin_monitoring.AdminMonitoringSummaryResponse;
import com.aditi_final.bridgeskill_api.dto.admin_monitoring.RecentApplicationResponse;
import com.aditi_final.bridgeskill_api.dto.admin_monitoring.RecentJobResponse;
import com.aditi_final.bridgeskill_api.entity.Application;
import com.aditi_final.bridgeskill_api.entity.ClientProfile;
import com.aditi_final.bridgeskill_api.entity.Job;
import com.aditi_final.bridgeskill_api.entity.User;
import com.aditi_final.bridgeskill_api.repository.ApplicationRepository;
import com.aditi_final.bridgeskill_api.repository.ClientProfileRepository;
import com.aditi_final.bridgeskill_api.repository.JobRepository;
import com.aditi_final.bridgeskill_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminMonitoringService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final ClientProfileRepository clientProfileRepository;

    public AdminMonitoringSummaryResponse getSummary() {
        long totalUsers = userRepository.count();

        long totalAdmins = userRepository.countByRoleId(1L);
        long totalStudents = userRepository.countByRoleId(2L);
        long totalClients = userRepository.countByRoleId(3L);

        long totalJobs = jobRepository.count();
        long totalApplications = applicationRepository.count();

        long pendingApplications = applicationRepository.countByStatus("PENDING");
        long reviewedApplications = applicationRepository.countByStatus("REVIEWED");
        long shortlistedApplications = applicationRepository.countByStatus("SHORTLISTED");
        long rejectedApplications = applicationRepository.countByStatus("REJECTED");

        List<RecentApplicationResponse> recentApplications = applicationRepository
                .findTop5ByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapRecentApplication)
                .toList();

        List<RecentJobResponse> recentJobs = jobRepository
                .findTop5ByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapRecentJob)
                .toList();

        return AdminMonitoringSummaryResponse.builder()
                .totalUsers(totalUsers)
                .totalAdmins(totalAdmins)
                .totalStudents(totalStudents)
                .totalClients(totalClients)
                .totalJobs(totalJobs)
                .totalApplications(totalApplications)
                .pendingApplications(pendingApplications)
                .reviewedApplications(reviewedApplications)
                .shortlistedApplications(shortlistedApplications)
                .rejectedApplications(rejectedApplications)
                .recentApplications(recentApplications)
                .recentJobs(recentJobs)
                .build();
    }

    private RecentApplicationResponse mapRecentApplication(Application application) {
        Job job = jobRepository.findById(application.getJobId()).orElse(null);
        User student = userRepository.findById(application.getStudentId()).orElse(null);

        String jobTitle = "Deleted Job";
        if (job != null && job.getTitle() != null && !job.getTitle().trim().isEmpty()) {
            jobTitle = job.getTitle();
        }

        String studentName = "Unknown Student";
        if (student != null && student.getFullName() != null && !student.getFullName().trim().isEmpty()) {
            studentName = student.getFullName();
        }

        return RecentApplicationResponse.builder()
                .id(application.getId())
                .jobId(application.getJobId())
                .jobTitle(jobTitle)
                .studentId(application.getStudentId())
                .studentName(studentName)
                .status(application.getStatus())
                .appliedAt(application.getCreatedAt())
                .build();
    }

    private RecentJobResponse mapRecentJob(Job job) {
        ClientProfile clientProfile = clientProfileRepository.findByUserId(job.getClientId()).orElse(null);

        String companyName = "Unknown Company";
        if (clientProfile != null
                && clientProfile.getCompanyName() != null
                && !clientProfile.getCompanyName().trim().isEmpty()) {
            companyName = clientProfile.getCompanyName();
        }

        return RecentJobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .clientId(job.getClientId())
                .companyName(companyName)
                .createdAt(job.getCreatedAt())
                .build();
    }
}