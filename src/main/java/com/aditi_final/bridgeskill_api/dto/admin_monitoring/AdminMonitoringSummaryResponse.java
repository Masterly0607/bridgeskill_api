package com.aditi_final.bridgeskill_api.dto.admin_monitoring;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminMonitoringSummaryResponse {

    private long totalUsers;
    private long totalStudents;
    private long totalClients;
    private long totalAdmins;

    private long totalJobs;
    private long totalApplications;

    private long pendingApplications;
    private long reviewedApplications;
    private long shortlistedApplications;
    private long rejectedApplications;

    private List<RecentApplicationResponse> recentApplications;
    private List<RecentJobResponse> recentJobs;
}