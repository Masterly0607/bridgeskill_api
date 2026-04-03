package com.aditi_final.bridgeskill_api.dto.client_profile;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ClientProfileResponse {
    private Long id;
    private Long userId;
    private String companyName;
    private String companyDescription;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}