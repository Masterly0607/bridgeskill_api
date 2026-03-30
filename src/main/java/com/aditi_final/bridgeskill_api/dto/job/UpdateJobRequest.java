package com.aditi_final.bridgeskill_api.dto.job;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateJobRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 150, message = "Title must not exceed 150 characters")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @Size(max = 100, message = "Category must not exceed 100 characters")
    private String category;

    @Size(max = 150, message = "Location must not exceed 150 characters")
    private String location;

    @DecimalMin(value = "0.0", inclusive = true, message = "Salary must be zero or greater")
    private BigDecimal salary;

    @NotBlank(message = "Status is required")
    private String status;
}