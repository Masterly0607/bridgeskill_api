package com.aditi_final.bridgeskill_api.dto.admin_monitoring;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecentJobResponse {
    private Long id;
    private String title;
    private Long clientId;
    private String companyName;
    private LocalDateTime createdAt;
}