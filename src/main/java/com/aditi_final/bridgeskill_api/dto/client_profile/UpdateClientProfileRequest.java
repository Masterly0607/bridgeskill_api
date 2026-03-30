package com.aditi_final.bridgeskill_api.dto.client_profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateClientProfileRequest {

    @NotBlank(message = "Company name is required")
    @Size(max = 150, message = "Company name must not exceed 150 characters")
    private String companyName;

    private String companyDescription;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;
}