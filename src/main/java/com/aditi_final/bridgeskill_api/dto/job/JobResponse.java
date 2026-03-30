package com.aditi_final.bridgeskill_api.dto.job;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class JobResponse {

    private Long id;
    private Long clientId;
    private String title;
    private String description;
    private String category;
    private String location;
    private BigDecimal salary;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}