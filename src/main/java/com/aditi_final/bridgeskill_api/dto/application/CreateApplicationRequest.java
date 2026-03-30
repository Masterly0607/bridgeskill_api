package com.aditi_final.bridgeskill_api.dto.application;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateApplicationRequest {

    @Size(max = 3000, message = "Cover letter must not exceed 3000 characters")
    private String coverLetter;
}