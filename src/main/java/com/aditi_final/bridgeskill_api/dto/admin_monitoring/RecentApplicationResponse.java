package com.aditi_final.bridgeskill_api.dto.admin_monitoring;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecentApplicationResponse {
    private Long id;
    private Long jobId;
    private String jobTitle;
    private Long studentId;
    private String studentName;
    private String status;
    private LocalDateTime appliedAt;
}