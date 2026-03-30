package com.aditi_final.bridgeskill_api.dto.application;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateApplicationStatusRequest {

    @NotBlank(message = "Status is required")
    private String status;
}