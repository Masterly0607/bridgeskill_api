package com.aditi_final.bridgeskill_api.controller;

import com.aditi_final.bridgeskill_api.dto.application.ApplicationResponse;
import com.aditi_final.bridgeskill_api.dto.application.CreateApplicationRequest;
import com.aditi_final.bridgeskill_api.dto.application.UpdateApplicationStatusRequest;
import com.aditi_final.bridgeskill_api.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/jobs/{jobId}")
    public ResponseEntity<ApplicationResponse> applyToJob(
            @PathVariable Long jobId,
            @Valid @RequestBody CreateApplicationRequest request
    ) {
        return ResponseEntity.ok(applicationService.applyToJob(jobId, request));
    }

    @GetMapping("/me")
    public ResponseEntity<List<ApplicationResponse>> getMyApplications() {
        return ResponseEntity.ok(applicationService.getMyApplications());
    }

    @GetMapping("/me/{id}")
    public ResponseEntity<ApplicationResponse> getMyApplicationById(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.getMyApplicationById(id));
    }

    @GetMapping("/client/jobs/{jobId}")
    public ResponseEntity<List<ApplicationResponse>> getApplicationsForMyJob(@PathVariable Long jobId) {
        return ResponseEntity.ok(applicationService.getApplicationsForMyJob(jobId));
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<ApplicationResponse> getApplicationForMyJob(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.getApplicationForMyJob(id));
    }

    @PutMapping("/client/{id}/status")
    public ResponseEntity<ApplicationResponse> updateApplicationStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateApplicationStatusRequest request
    ) {
        return ResponseEntity.ok(applicationService.updateApplicationStatus(id, request));
    }

    @GetMapping("/admin/all")
    public ResponseEntity<List<ApplicationResponse>> getAllApplications() {
        return ResponseEntity.ok(applicationService.getAllApplications());
    }
}