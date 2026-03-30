package com.aditi_final.bridgeskill_api.service;

import com.aditi_final.bridgeskill_api.dto.job.CreateJobRequest;
import com.aditi_final.bridgeskill_api.dto.job.JobResponse;
import com.aditi_final.bridgeskill_api.dto.job.UpdateJobRequest;
import com.aditi_final.bridgeskill_api.entity.Job;
import com.aditi_final.bridgeskill_api.entity.User;
import com.aditi_final.bridgeskill_api.repository.JobRepository;
import com.aditi_final.bridgeskill_api.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public JobService(JobRepository jobRepository, UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    public List<JobResponse> getAllJobs() {
        return jobRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public JobResponse getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));

        return mapToResponse(job);
    }

    public JobResponse createJob(CreateJobRequest request, String email) {
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));

        if (!job.getClientId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only update your own jobs");
        }

        if (!request.getStatus().equals("OPEN") && !request.getStatus().equals("CLOSED")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid job status");
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));

        if (!job.getClientId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only delete your own jobs");
        }

        jobRepository.delete(job);
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