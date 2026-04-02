package com.aditi_final.bridgeskill_api.service;

import com.aditi_final.bridgeskill_api.dto.job.CreateJobRequest;
import com.aditi_final.bridgeskill_api.dto.job.JobResponse;
import com.aditi_final.bridgeskill_api.dto.job.UpdateJobRequest;
import com.aditi_final.bridgeskill_api.entity.Job;
import com.aditi_final.bridgeskill_api.entity.User;
import com.aditi_final.bridgeskill_api.exception.ForbiddenActionException;
import com.aditi_final.bridgeskill_api.exception.ResourceNotFoundException;
import com.aditi_final.bridgeskill_api.repository.ApplicationRepository;
import com.aditi_final.bridgeskill_api.repository.JobRepository;
import com.aditi_final.bridgeskill_api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;

    public JobService(
            JobRepository jobRepository,
            UserRepository userRepository,
            ApplicationRepository applicationRepository
    ) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
    }

    public List<JobResponse> getAllJobs(String keyword, String category, String status) {
        String normalizedKeyword = normalize(keyword);
        String normalizedCategory = normalize(category);
        String normalizedStatus = normalize(status);

        return jobRepository.findAll()
                .stream()
                .filter(job -> normalizedKeyword == null
                        || (job.getTitle() != null
                        && job.getTitle().toLowerCase().contains(normalizedKeyword.toLowerCase())))
                .filter(job -> normalizedCategory == null
                        || (job.getCategory() != null
                        && job.getCategory().equalsIgnoreCase(normalizedCategory)))
                .filter(job -> normalizedStatus == null
                        || (job.getStatus() != null
                        && job.getStatus().equalsIgnoreCase(normalizedStatus)))
                .sorted(Comparator.comparing(Job::getCreatedAt,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .map(this::mapToResponse)
                .toList();
    }

    public JobResponse getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        return mapToResponse(job);
    }

    public JobResponse createJob(CreateJobRequest request, String email) {
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Job job = Job.builder()
                .clientId(currentUser.getId())
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .location(request.getLocation())
                .salary(request.getSalary())
                .status("OPEN")
                .build();

        Job savedJob = jobRepository.save(job);
        return mapToResponse(savedJob);
    }

    public JobResponse updateJob(Long id, UpdateJobRequest request, String email) {
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        if (!job.getClientId().equals(currentUser.getId())) {
            throw new ForbiddenActionException("You can only update your own jobs");
        }

        if (!request.getStatus().equals("OPEN") && !request.getStatus().equals("CLOSED")) {
            throw new IllegalArgumentException("Invalid job status");
        }

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setCategory(request.getCategory());
        job.setLocation(request.getLocation());
        job.setSalary(request.getSalary());
        job.setStatus(request.getStatus());

        Job updatedJob = jobRepository.save(job);
        return mapToResponse(updatedJob);
    }

    public void deleteJob(Long id, String email) {
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        if (!job.getClientId().equals(currentUser.getId())) {
            throw new ForbiddenActionException("You can only delete your own jobs");
        }

        boolean hasApplications = applicationRepository.existsByJobId(id);

        if (hasApplications) {
            job.setStatus("CLOSED");
            jobRepository.save(job);
            return;
        }

        job.setStatus("CLOSED");
        jobRepository.save(job);
    }

    private String normalize(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.trim();
    }

    private JobResponse mapToResponse(Job job) {
        return JobResponse.builder()
                .id(job.getId())
                .clientId(job.getClientId())
                .title(job.getTitle())
                .description(job.getDescription())
                .category(job.getCategory())
                .location(job.getLocation())
                .salary(job.getSalary())
                .status(job.getStatus())
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();
    }
}