package com.aditi_final.bridgeskill_api.controller;

import com.aditi_final.bridgeskill_api.dto.admin_monitoring.AdminMonitoringSummaryResponse;
import com.aditi_final.bridgeskill_api.service.AdminMonitoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/monitoring")
@RequiredArgsConstructor
public class AdminMonitoringController {

    private final AdminMonitoringService adminMonitoringService;

    @GetMapping("/summary")
    public ResponseEntity<AdminMonitoringSummaryResponse> getSummary() {
        return ResponseEntity.ok(adminMonitoringService.getSummary());
    }
}