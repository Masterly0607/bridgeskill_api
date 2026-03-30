package com.aditi_final.bridgeskill_api.controller;

import com.aditi_final.bridgeskill_api.dto.job.CreateJobRequest;
import com.aditi_final.bridgeskill_api.dto.job.JobResponse;
import com.aditi_final.bridgeskill_api.dto.job.UpdateJobRequest;
import com.aditi_final.bridgeskill_api.service.JobService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public List<JobResponse> getAllJobs() {
        return jobService.getAllJobs();
    }

    @GetMapping("/{id}")
    public JobResponse getJobById(@PathVariable Long id) {
        return jobService.getJobById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobResponse createJob(@Valid @RequestBody CreateJobRequest request,
                                 Authentication authentication) {
        return jobService.createJob(request, authentication.getName());
    }

    @PutMapping("/{id}")
    public JobResponse updateJob(@PathVariable Long id,
                                 @Valid @RequestBody UpdateJobRequest request,
                                 Authentication authentication) {
        return jobService.updateJob(id, request, authentication.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteJob(@PathVariable Long id, Authentication authentication) {
        jobService.deleteJob(id, authentication.getName());
    }
}